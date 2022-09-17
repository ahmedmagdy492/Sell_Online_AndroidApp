package com.magdyradwan.sellonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.helpers.NetworkConnectionChecker;
import com.magdyradwan.sellonline.irepository.IAuthRepo;
import com.magdyradwan.sellonline.repository.AuthRepo;
import com.magdyradwan.sellonline.responsemodels.LoginResponseModel;
import com.magdyradwan.sellonline.viewmodels.LoginViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btnLogin;
    private TextView email;
    private TextView password;
    private ScrollView main_parent;
    private IAuthRepo authRepo = null;

    private void initViews() {
        btnLogin = findViewById(R.id.btn_login);
        email = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_password);
        main_parent = findViewById(R.id.main_parent);
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

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                if(authRepo == null) {
                    HttpClient httpClient = new HttpClient(getApplicationContext(),
                            getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

                    authRepo = new AuthRepo(httpClient);
                }

                boolean result = authRepo.checkForTokenValidity();

                if(result) {
                    // token is valid
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    runOnUiThread(() -> {
                        RelativeLayout relativeLayout = findViewById(R.id.loader_overlay);
                        relativeLayout.setVisibility(View.GONE);
                        main_parent.setVisibility(View.VISIBLE);
                    });
                }
            }
            catch (IOException | UnAuthorizedException e) {
                runOnUiThread(() -> {
                    RelativeLayout relativeLayout = findViewById(R.id.loader_overlay);
                    relativeLayout.setVisibility(View.GONE);
                    main_parent.setVisibility(View.VISIBLE);
                });
            }
            catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
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
                        if(authRepo == null)
                        {
                            HttpClient httpClient = new HttpClient(getApplicationContext(),
                                    getSharedPreferences("userData", MODE_PRIVATE));

                            authRepo = new AuthRepo(httpClient);
                        }

                        LoginResponseModel responseModel = authRepo.login(model);

                        // check for valid token returned by API
                        if(responseModel.getToken().equals("")) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Login Failed due to unexpected error", Toast.LENGTH_SHORT).show();
                                btnLogin.setEnabled(true);
                                btnLogin.setText(getString(R.string.login));
                            });
                        }
                        else {
                            setData("token", responseModel.getToken());
                            setData("userId", responseModel.getUserId());

                            // goto to the next activity
                            runOnUiThread(() -> {
                                Intent intent = new Intent(this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        }

                    }
                    catch(UnAuthorizedException | IOException | JSONException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            btnLogin.setEnabled(true);
                            btnLogin.setText(getString(R.string.login));
                        });
                    }
                    catch (NoInternetException e) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, NoInternetActivity.class);
                            startActivity(intent);
                        });
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