package com.magdyradwan.sellonline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.ProfileJsonReader;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private Handler mHandler;
    private boolean isFragmentOpened = false;

    private void loadProfileData() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                        getString(R.string.preference_key),
                        MODE_PRIVATE
                ));

                String response = httpClient.getRequest("Users/Profile");
                ProfileJsonReader profileJsonReader = new ProfileJsonReader();
                ProfileResponseModel profileResponseModel = profileJsonReader.ReadJson(response).get(0);

                mHandler.post(() -> {
                    TextView displayName = findViewById(R.id.display_name);
                    displayName.setText(profileResponseModel.getDisplayName());

                    showProfileData(profileResponseModel);
                });
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onLoad: " + e.getMessage(), e);
            }
        });
    }

    private void showProfileData(ProfileResponseModel profileResponseModel) {
        ImageView menuBtn = findViewById(R.id.profile_menu);
        ArrayList<String> phoneNumbers = new ArrayList<>();

        for(int i = 0;i < profileResponseModel.getPhoneNumbers().size(); i++) {
            phoneNumbers.add(profileResponseModel.getPhoneNumbers().get(i).getPhoneNumber());
        }

        menuBtn.setOnClickListener(v -> {
            if(!isFragmentOpened) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile_container,
                                ProfileFragment.newInstance(
                                        profileResponseModel.getDisplayName(),
                                        profileResponseModel.getEmail(),
                                        profileResponseModel.getCountry(),
                                        profileResponseModel.getCity(),
                                        profileResponseModel.getDistrict(),
                                        profileResponseModel.getUserId(),
                                        phoneNumbers)
                        )
                        .addToBackStack(null)
                        .commit();
                isFragmentOpened = true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mHandler = new Handler(getMainLooper());

        loadProfileData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isFragmentOpened = false;

        //finishAffinity();
    }
}