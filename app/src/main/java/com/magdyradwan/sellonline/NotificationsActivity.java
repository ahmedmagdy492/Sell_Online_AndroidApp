package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.NotificationJsonReader;
import com.magdyradwan.sellonline.responsemodels.NotificationResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";

    private SharedPreferences sharedPreferences;
    private ProgressBar noti_loader;

    private List<NotificationResponseModel> getMyNotifications () throws IOException, UnAuthorizedException, JSONException {
        HttpClient httpClient = new HttpClient(NotificationsActivity.this,
                sharedPreferences
        );

        String response = httpClient.getRequest("Notification");
        Log.d(TAG, "getMyNotifications: response " + response);
        NotificationJsonReader notificationJsonReader = new NotificationJsonReader();
        return notificationJsonReader.ReadJson(response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.notifications));
        }

        noti_loader = findViewById(R.id.noti_loader);
        ListView list = findViewById(R.id.notifications_list);

        sharedPreferences = getSharedPreferences(getString(R.string.preference_key),
                MODE_PRIVATE);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                List<NotificationResponseModel> notifications = getMyNotifications();

                runOnUiThread(() -> {
                    LinearLayout txt = findViewById(R.id.empty_view);

                    noti_loader.setVisibility(View.GONE);
                    if(notifications.size() == 0) {
                        txt.setVisibility(View.VISIBLE);
                    }
                    else {
                        list.setAdapter(new NotificationAdapter(this, R.layout.notification_item
                                , notifications));
                    }
                });
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    noti_loader.setVisibility(View.GONE);
                });
            }
        });
    }
}