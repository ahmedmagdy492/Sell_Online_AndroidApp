package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.magdyradwan.sellonline.adapters.PostsAdapter;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IPostsRepo;
import com.magdyradwan.sellonline.jsonreaders.PostsJsonReader;
import com.magdyradwan.sellonline.repository.PostsRepo;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recView = findViewById(R.id.searched_posts);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.search_for_posts));
        }

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search(query);
        }
    }

    private void search(String query) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                HttpClient httpClient = new HttpClient(
                        SearchActivity.this, getSharedPreferences(
                        getString(R.string.preference_key),MODE_PRIVATE)
                );

                IPostsRepo postsRepo = new PostsRepo(httpClient);

                ArrayList<PostResponseModel> posts = postsRepo.searchPosts(query);

                if(posts.size() > 0) {
                    runOnUiThread(() -> {
                        recView.setAdapter(new PostsAdapter(SearchActivity.this, posts));
                        recView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    });
                }
                else {
                    runOnUiThread(() -> {
                        ImageView imageView = findViewById(R.id.no_results);
                        imageView.setVisibility(View.VISIBLE);
                    });
                }
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(SearchActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });
    }
}