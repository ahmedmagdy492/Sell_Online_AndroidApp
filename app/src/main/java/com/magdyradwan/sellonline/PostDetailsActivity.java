package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.sellonline.dto.ImageUploadDTO;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.jsonreaders.PostDetailsJsonReader;
import com.magdyradwan.sellonline.jsonreaders.PostImageJSONReader;
import com.magdyradwan.sellonline.jsonreaders.PostsJsonReader;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private GridView images_list;

    private PostResponseModel getPostDetails(String postId) throws IOException, UnAuthorizedException, JSONException, NoInternetException {
        HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                getString(R.string.preference_key),
                MODE_PRIVATE
        ));

        String response = httpClient.getRequest("Posts/" + postId);
        PostDetailsJsonReader postsJsonReader = new PostDetailsJsonReader();
        return postsJsonReader.ReadJson(response).get(0);
    }

    private void addViewToPost(String postId) throws IOException, UnAuthorizedException, NoInternetException {
        HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                getString(R.string.preference_key),
                MODE_PRIVATE
                ));

        httpClient.postRequest("Posts/Views/Add?postId=" + postId, "");
    }

    private ArrayList<PostImageResponseModel> getImagesOfPost(String postID) throws IOException, UnAuthorizedException, JSONException, NoInternetException {
        HttpClient httpClient = new HttpClient(
                PostDetailsActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        String response = httpClient.getRequest("Posts/Images?postID=" + postID);
        PostImageJSONReader postImageJSONReader = new PostImageJSONReader();
        return postImageJSONReader.ReadJson(response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        images_list = findViewById(R.id.images_list);

        ExecutorService service = Executors.newFixedThreadPool(3);
        String postId = getIntent().getStringExtra("post_id");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Issue a web request to get the post details
            service.execute(() -> {
                try {
                    PostResponseModel post = getPostDetails(postId);

                    runOnUiThread(() -> {
                        actionBar.setTitle(post.getTitle());
                        updateUI(post);
                    });

                } catch (IOException | UnAuthorizedException | JSONException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } catch (NoInternetException e) {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(PostDetailsActivity.this, NoInternetActivity.class);
                        startActivity(intent);
                    });
                }
            });
        }

        service.execute(() -> {
            try {
                addViewToPost(postId);
            } catch (IOException | UnAuthorizedException e) {

                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(PostDetailsActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });

        service.execute(() -> {
            try {
                ArrayList<PostImageResponseModel> postImages = getImagesOfPost(postId);

                runOnUiThread(() -> {
                    images_list.setAdapter(new PostImageAdapter(PostDetailsActivity.this, R.layout.image_upload_item, postImages));
                    images_list.setOnItemClickListener((parent, view, position, id) -> {
                        Intent intent = new Intent(PostDetailsActivity.this, ImageSliderActivity.class);
                        intent.putExtra("postID", postId);
                        startActivity(intent);
                    });
                });
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(PostDetailsActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });
    }

    private void updateUI(PostResponseModel post) {
        TextView displayName = findViewById(R.id.display_name_user_post_details);
        displayName.setText(post.getUser().getDisplayName());

        ImageView profileImg = findViewById(R.id.user_profile_image_details);
        Log.d(TAG, "updateUI: " + post.getUser().getProfileImageURL());
        byte[] imgBytes =
                Base64Converter.convertFromBase64ToByteArr(post.getUser().getProfileImageURL());
        Bitmap bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        profileImg.setImageBitmap(bmp);

        TextView viewCount = findViewById(R.id.views_count_details);
        if(post.getPostViews().size() == 1) {
            viewCount.setText("Viewed By " + post.getPostViews().size() + " User");
        }
        else {
            viewCount.setText("Viewed By " + post.getPostViews().size() + " Users");
        }

        TextView title = findViewById(R.id.post_title_details);
        title.setText(post.getTitle());

        TextView category = findViewById(R.id.category_name_details);
        category.setText(post.getPostCategory().getName());

        TextView content = findViewById(R.id.post_content_details);
        content.setText(post.getContent());

        ImageButton btnShare = findViewById(R.id.share_btn);
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
            intent.putExtra(android.content.Intent.EXTRA_TEXT, post.getContent());

            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        });
    }
}