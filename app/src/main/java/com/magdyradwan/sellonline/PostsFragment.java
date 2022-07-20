package com.magdyradwan.sellonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.PostsJsonReader;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment";
    private int pageNo = 1;

    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View holder = inflater.inflate(R.layout.fragment_posts, container, false);
        ProgressBar loader = holder.findViewById(R.id.posts_loader);

        RecyclerView postsList = holder.findViewById(R.id.posts_list);
        postsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                HttpClient httpClient = new HttpClient(getActivity(), getActivity().getSharedPreferences(
                        getString(R.string.preference_key),
                        Context.MODE_PRIVATE
                ));

                String response =
                        httpClient.getRequest("Posts/Trending?pageNo=" + pageNo + "&pageSize=10");
                PostsJsonReader postsJsonReader = new PostsJsonReader();
                ArrayList<PostResponseModel> trendingPosts = postsJsonReader.ReadJson(response);

                getActivity().runOnUiThread(() -> {
                    loader.setVisibility(View.GONE);
                    postsList.setAdapter(new PostsAdapter(getActivity(), trendingPosts));
                });

            } catch (IOException | UnAuthorizedException | JSONException e) {
                Log.e(TAG, "onCreateView: " + e.getMessage(), e);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                });
            } catch (NoInternetException e) {
                getActivity().runOnUiThread(() -> {
                    Intent intent = new Intent(getActivity(), NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });

        return holder;
    }
}