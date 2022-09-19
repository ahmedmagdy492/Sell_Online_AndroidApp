package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.magdyradwan.sellonline.adapters.NotificationAdapter;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.INotificationRepo;
import com.magdyradwan.sellonline.repository.NotificationRepo;
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
                HttpClient httpClient = new HttpClient(NotificationsActivity.this,
                        sharedPreferences
                );

                INotificationRepo notificationRepo = new NotificationRepo(httpClient);

                List<NotificationResponseModel> notifications = notificationRepo.getMyNotifications();

                runOnUiThread(() -> {
                    ImageView txt = findViewById(R.id.empty_view);

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
            } catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(NotificationsActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });
    }
}