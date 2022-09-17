package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.models.ChangePasswordModel;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button btn_confirm;

    private void initViews() {
        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        confirmNewPassword = findViewById(R.id.confirm_new_password);
        btn_confirm = findViewById(R.id.btn_changePassword);
    }

    private boolean validateInput() {
        if(currentPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(newPassword.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!confirmNewPassword.getText().toString().equals(newPassword.getText().toString())) {
            Toast.makeText(this, "New password and confirm new password must match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean changePassword(ChangePasswordModel model) throws IOException, NoInternetException, UnAuthorizedException {
        HttpClient httpClient = new HttpClient(ChangePasswordActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        httpClient.postRequest("Auth/ChangePassword", model.convertToJson());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(getString(R.string.change_password));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        initViews();

        btn_confirm.setOnClickListener(v -> {
            if(validateInput()) {
                ChangePasswordModel changePasswordModel = new ChangePasswordModel(
                        currentPassword.getText().toString(),
                        newPassword.getText().toString(),
                        confirmNewPassword.getText().toString()
                );

                // confirm changing the password
                executorService.execute(() -> {
                    boolean result = false;
                    try {
                        result = changePassword(changePasswordModel);
                    } catch (IOException | UnAuthorizedException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } catch (NoInternetException e) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(ChangePasswordActivity.this, NoInternetActivity.class);
                            startActivity(intent);
                        });
                    }

                    if(result) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Password has been changed and changes will be reflected in the next login", Toast.LENGTH_SHORT).show();
                            this.finish();
                        });
                    }
                });
            }
        });
    }
}