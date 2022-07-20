package com.magdyradwan.sellonline;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magdyradwan.sellonline.dto.ImageUploadDTO;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.helpers.FileReaderHelper;
import com.magdyradwan.sellonline.jsonreaders.CategoryListJsonReader;
import com.magdyradwan.sellonline.jsonreaders.CreatePostJsonReader;
import com.magdyradwan.sellonline.jsonreaders.PostDetailsJsonReader;
import com.magdyradwan.sellonline.jsonreaders.PostImageJSONReader;
import com.magdyradwan.sellonline.jsonreaders.PostsJsonReader;
import com.magdyradwan.sellonline.models.CreatePostModel;
import com.magdyradwan.sellonline.models.EditPostModel;
import com.magdyradwan.sellonline.models.UploadImageModel;
import com.magdyradwan.sellonline.responsemodels.PostCategory;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostEditActivity extends AppCompatActivity {

    private static final String TAG = "PostEditActivity";
    private String postID;

    private EditText title;
    private EditText content;
    private Spinner categoryList;
    private FloatingActionButton btnSave;
    private GridView gridView;
    private Button btnUploadImage;

    private void initViews() {
        title = findViewById(R.id.post_title_create);
        content = findViewById(R.id.post_content_create);
        categoryList = findViewById(R.id.post_edit_spinner);
        btnSave = findViewById(R.id.save_post);
        btnUploadImage = findViewById(R.id.upload_image);

        gridView = findViewById(R.id.images_layout_2);

        btnSave.setEnabled(false);
    }



    private boolean validateInputs() {

        if(title.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Convenient Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(content.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Descriptive Content for the Post", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private PostResponseModel getPostByID() throws IOException, UnAuthorizedException, JSONException {
        HttpClient httpClient = new HttpClient(
                PostEditActivity.this,
                getSharedPreferences(getString(R.string.preference_key),
                        MODE_PRIVATE
                ));
        String response = httpClient.getRequest("Posts/" + postID);
        PostDetailsJsonReader reader = new PostDetailsJsonReader();
        ArrayList<PostResponseModel> posts = reader.ReadJson(response);
        return posts.get(0);
    }


    private ArrayList<PostCategory> getCategoryList() throws IOException, UnAuthorizedException, JSONException {
        HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                getString(R.string.preference_key),
                MODE_PRIVATE
        ));

        String response = httpClient.getRequest("Lookups/Categories");
        CategoryListJsonReader categoryListJsonReader = new CategoryListJsonReader();
        return categoryListJsonReader.ReadJson(response);
    }

    private ArrayList<PostImageResponseModel> getImagesOfPost(String postID) throws IOException, UnAuthorizedException, JSONException {
        HttpClient httpClient = new HttpClient(
                PostEditActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        String response = httpClient.getRequest("Posts/Images?postID=" + postID);
        PostImageJSONReader postImageJSONReader = new PostImageJSONReader();
        return postImageJSONReader.ReadJson(response);
    }

    private boolean editPost() throws IOException, UnAuthorizedException, JSONException {
        TextView categoryId = categoryList.getSelectedView().findViewById(R.id.category_id_spinner);
        EditPostModel postModel = new EditPostModel(
                postID,
                title.getText().toString(),
                content.getText().toString(),
                Integer.parseInt(categoryId.getText().toString())
        );
        HttpClient httpClient = new HttpClient(PostEditActivity.this, getSharedPreferences(
                getString(R.string.preference_key),
                MODE_PRIVATE
        ));

        httpClient.patchRequest("Posts/Update?postId=" + postID, postModel.convertToJson());
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        postID = getIntent().getStringExtra("post_id");

        initViews();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.edit_post));
        }

        // getting the post data by id
        executorService.execute(() -> {
            try {
                PostResponseModel post = getPostByID();

                runOnUiThread(() -> {
                    //TextView categoryId = categoryList.getSelectedView().findViewById(R.id.category_id_spinner);
                    title.setText(post.getTitle());
                    content.setText(post.getContent());
                    //categoryId.setText(String.valueOf(post.getPostCategoryID()));
                    //categoryList.setSelection((int)post.getPostCategoryID());
                });

            } catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        // get category list
        executorService.execute(() -> {
            try {
                ArrayList<PostCategory> categories = getCategoryList();

                runOnUiThread(() -> {
                    categoryList.setAdapter(new CategoryAdapter(PostEditActivity.this,
                            R.layout.spinner_item, categories));
                    btnSave.setEnabled(true);
                });

            } catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(PostEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                });
            }
        });

        // get images of the current post
        executorService.execute(() -> {
            try {
                ArrayList<PostImageResponseModel> postImages = getImagesOfPost(postID);

                runOnUiThread(() -> {
                    ArrayList<Bitmap> bmps = new ArrayList<>();
                    for(PostImageResponseModel img : postImages) {
                        byte[] arr = Base64Converter.convertFromBase64ToByteArr(img.getImageURL());
                        bmps.add(BitmapFactory.decodeByteArray(arr, 0, arr.length));
                    }

                    gridView.setAdapter(new ReadOnlyImagesAdapter(PostEditActivity.this,
                            R.layout.image_upload_item, bmps));
                });
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

        // saving the post data
        btnSave.setOnClickListener(v -> executorService.execute(() -> {
            try {
                if(validateInputs()) {
                    boolean result = editPost();
                    if (result) {
                        runOnUiThread(() -> {
                            Toast.makeText(PostEditActivity.this, "Changes has been saved", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }
                }
            } catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> Toast.makeText(PostEditActivity.this,
                        e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }));
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}