package com.magdyradwan.sellonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IPostsRepo;
import com.magdyradwan.sellonline.repository.PostsRepo;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageSliderActivity extends AppCompatActivity {

    private static final String TAG = "ImageSliderActivity";
    private ArrayList<Bitmap> post_images = new ArrayList<>();
    private int current_image_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);

        ImageView postImageSlider = findViewById(R.id.post_image_slider);

        String postID = getIntent().getStringExtra("postID");

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                HttpClient client = new HttpClient(
                        ImageSliderActivity.this,
                        getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE)
                );
                IPostsRepo postsRepo = new PostsRepo(client);

                ArrayList<PostImageResponseModel> images = postsRepo.getImagesOfPost(postID);

                for(PostImageResponseModel model : images) {
                    byte[] arr = Base64Converter.convertFromBase64ToByteArr(model.getImageURL());
                    post_images.add(BitmapFactory.decodeByteArray(arr, 0, arr.length));

                    runOnUiThread(() -> {
                        postImageSlider.setImageBitmap(post_images.get(current_image_index));
                    });
                }
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(ImageSliderActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });

        Button btnRight = findViewById(R.id.btn_right);
        Button btnLeft = findViewById(R.id.btn_left);

        btnRight.setOnClickListener(v -> {
            if(current_image_index < post_images.size() - 1) {
                current_image_index++;
            }
            else {
                current_image_index = 0;
            }
            postImageSlider.setImageBitmap(post_images.get(current_image_index));
        });

        btnLeft.setOnClickListener(v -> {
            if(current_image_index >= 1) {
                current_image_index--;
            }
            else {
                current_image_index = post_images.size() - 1;
            }
            postImageSlider.setImageBitmap(post_images.get(current_image_index));
        });
    }
}