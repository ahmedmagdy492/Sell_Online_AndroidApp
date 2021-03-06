package com.magdyradwan.sellonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.NetworkConnectionChecker;
import com.magdyradwan.sellonline.jsonreaders.LoginJsonReader;
import com.magdyradwan.sellonline.responsemodels.LoginResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btnLogin;
    private TextView email;
    private TextView password;
    private Handler mHandler;
    private HttpClient httpClient;

    private void initViews() {
        btnLogin = findViewById(R.id.btn_login);
        email = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_password);
    }

    private boolean checkForTokenValidity() throws IOException {
        try {
            HttpClient httpClient = new HttpClient(getApplicationContext(), getSharedPreferences(
                    getString(R.string.preference_key),
                    MODE_PRIVATE
            ));

            httpClient.getRequest("Auth/IsValid");
            return true;
        }
        catch (UnAuthorizedException e) {
            return false;
        } catch (NoInternetException e) {
            runOnUiThread(() -> {
                Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
                startActivity(intent);
            });
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        NetworkConnectionChecker networkConnectionChecker = new NetworkConnectionChecker();
        if(!networkConnectionChecker.isNetworkAvailable(getApplicationContext())) {
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            return;
        }

        mHandler = new Handler(getMainLooper());
        try {
            httpClient = new HttpClient(getApplicationContext(),
                    getSharedPreferences("userData", MODE_PRIVATE));
        }
        catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        } catch (NoInternetException e) {
            runOnUiThread(() -> {
                Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
                startActivity(intent);
            });
            return;
        }

        String token = getData("token");
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                boolean result = checkForTokenValidity();

                if(!token.equals("") && result) {
                    // token is valid
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    mHandler.post(() -> {
                        RelativeLayout relativeLayout = findViewById(R.id.loader_overlay);
                        relativeLayout.setVisibility(View.GONE);
                    });
                }
            } catch (IOException e) {
                Log.e(TAG, "onCreate: when checking the validity of the token", e);
            }
        });

        btnLogin.setOnClickListener(v -> {
            LoginViewModel model = new LoginViewModel(email.getText().toString(),
                    password.getText().toString());
            boolean isValid = validateInput(model);

            if(isValid) {
                btnLogin.setEnabled(false);
                btnLogin.setText(getString(R.string.loading));
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                executorService.execute(() -> {
                    try {
                        String response = httpClient.postRequest("Auth/Login", model.convertToJson());

                        Log.d(TAG, "after response: " + response);

                        LoginJsonReader loginJsonReader = new LoginJsonReader();
                        LoginResponseModel responseModel = loginJsonReader.ReadJson(response);

                        // TODO: check for valid token returned by API
                        if(responseModel.getToken().equals("")) {
                            mHandler.post(() -> {
                                Toast.makeText(this, "Login Failed due to unexpected error", Toast.LENGTH_SHORT).show();
                                btnLogin.setEnabled(true);
                                btnLogin.setText(getString(R.string.login));
                            });
                        }
                        else {
                            setData("token", responseModel.getToken());
                            setData("userId", responseModel.getUserId());

                            // goto to the next activity
                            Intent intent = new Intent(this, HomeActivity.class);
                            startActivity(intent);
                            mHandler.post(() -> finish());
                        }

                    }
                    catch(UnAuthorizedException e) {
                        mHandler.post(() -> {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            btnLogin.setEnabled(true);
                            btnLogin.setText(getString(R.string.login));
                        });
                    }
                    catch (IOException | JSONException e) {
                        mHandler.post(() -> {
                            btnLogin.setEnabled(true);
                            btnLogin.setText(getString(R.string.login));
                        });
                        Log.d(TAG, "while sending request: " + e.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private boolean validateInput(LoginViewModel loginViewModel) {
        if(loginViewModel.getEmail().equals("")) {
            Toast.makeText(this, "Please Enter your Email Address",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        else if(loginViewModel.getPassword().equals("")) {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void setData(String key, String value) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(
                this.getString(R.string.preference_key),
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_key),
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}