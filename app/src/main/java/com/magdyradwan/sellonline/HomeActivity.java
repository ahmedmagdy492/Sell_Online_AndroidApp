package com.magdyradwan.sellonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.fragments.MyPostsFragment;
import com.magdyradwan.sellonline.fragments.PostsFragment;
import com.magdyradwan.sellonline.fragments.ProfileFragment;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IUsersRepo;
import com.magdyradwan.sellonline.repository.UsersRepo;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FloatingActionButton fab_createPost;

    private void onBottomNavigationSelected() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getTitle().equals("Profile")) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    try {
                        HttpClient httpClient = new HttpClient(getApplicationContext(), getSharedPreferences(
                                getString(R.string.preference_key),
                                MODE_PRIVATE
                        ));

                        IUsersRepo usersRepo = new UsersRepo(httpClient);
                        ProfileResponseModel profileResponseModel = usersRepo.getUserProfile();

                        ArrayList<String> phoneNumbers = new ArrayList<>();

                        for(int i = 0;i < profileResponseModel.getPhoneNumbers().size(); i++) {
                            phoneNumbers.add(profileResponseModel.getPhoneNumbers().get(i).getPhoneNumber());
                        }

                        runOnUiThread(() -> {
                            ProfileFragment profileFragment = ProfileFragment.newInstance(
                                    profileResponseModel.getDisplayName(),
                                    profileResponseModel.getEmail(),
                                    profileResponseModel.getCountry(),
                                    profileResponseModel.getCity(),
                                    profileResponseModel.getDistrict(),
                                    profileResponseModel.getUserId(),
                                    phoneNumbers);

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.profile_container, profileFragment)
                                    .commit();
                            fab_createPost.setVisibility(View.GONE);
                        });
                    }
                    catch (IOException | UnAuthorizedException | JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onLoad: " + e.getMessage(), e);
                    } catch (NoInternetException e) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(HomeActivity.this, NoInternetActivity.class);
                            startActivity(intent);
                        });
                    }
                });
            }
            else if(item.getTitle().equals("Home")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile_container, new PostsFragment())
                        .commit();
                fab_createPost.setVisibility(View.VISIBLE);
            }
            else if(item.getTitle().equals("My Posts")) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.profile_container, new MyPostsFragment())
                        .commit();
                fab_createPost.setVisibility(View.VISIBLE);
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.log_out_btn) {
            SharedPreferences sharedPreferences = this.
                    getSharedPreferences(this.getString(R.string.preference_key),
                            Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", "");
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.notifications) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.search_posts) {
            onSearchRequested();
        }
        else if(item.getItemId() == R.id.messages)
        {
            Intent intent = new Intent(this, ChatsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fab_createPost = findViewById(R.id.create_new_post);

        fab_createPost.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivity(intent);
        });

        onBottomNavigationSelected();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }
}