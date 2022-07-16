package com.magdyradwan.sellonline;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.PostsJsonReader;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyPostsFragment extends Fragment {

    private RecyclerView my_posts_list;
    private static final String TAG = "MyPostsFragment";
    private final int pageNo = 1;
    private ProgressBar loader;

    public MyPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_my_posts, container, false);

        my_posts_list = inflatedView.findViewById(R.id.my_posts_list);
        my_posts_list.setLayoutManager(new LinearLayoutManager(getActivity()));

        loader = inflatedView.findViewById(R.id.loader);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                HttpClient httpClient = new HttpClient(getActivity(), getActivity().getSharedPreferences(
                        getString(R.string.preference_key),
                        Context.MODE_PRIVATE
                ));

                String response =
                        httpClient.getRequest("Posts/MyPosts?pageNo=" + pageNo + "&pageSize=10");
                PostsJsonReader postsJsonReader = new PostsJsonReader();
                ArrayList<PostResponseModel> trendingPosts = postsJsonReader.ReadJson(response);

                getActivity().runOnUiThread(() -> {
                    loader.setVisibility(View.GONE);
                    my_posts_list.setAdapter(new MyPostsAdapter(getActivity(), trendingPosts));
                });

            } catch (IOException | UnAuthorizedException | JSONException e) {
                Log.e(TAG, "onCreateView: " + e.getMessage(), e);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                });
            }
        });

        return inflatedView;
    }
}