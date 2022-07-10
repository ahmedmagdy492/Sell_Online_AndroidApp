package com.magdyradwan.sellonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.LoginJsonReader;
import com.magdyradwan.sellonline.responsemodels.LoginResponseModel;

import org.json.JSONException;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mHandler = new Handler(getMainLooper());
        try {
            httpClient = new HttpClient(getApplicationContext(),
                    getSharedPreferences("userData", MODE_PRIVATE));
        }
        catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

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