package com.magdyradwan.sellonline;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magdyradwan.sellonline.adapters.CategoryAdapter;
import com.magdyradwan.sellonline.adapters.ImagesAdapter;
import com.magdyradwan.sellonline.dto.ImageUploadDTO;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.helpers.FileReaderHelper;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.ILookupRepo;
import com.magdyradwan.sellonline.irepository.IPostsRepo;
import com.magdyradwan.sellonline.models.CreatePostModel;
import com.magdyradwan.sellonline.models.UploadImageModel;
import com.magdyradwan.sellonline.repository.LookupRepo;
import com.magdyradwan.sellonline.repository.PostsRepo;
import com.magdyradwan.sellonline.responsemodels.PostCategory;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreatePostActivity extends AppCompatActivity {

    private static final String TAG = "CreatePostActivity";

    private IPostsRepo postsRepo;
    private List<ImageUploadDTO> images = new ArrayList<>();
    private EditText title;
    private EditText content;
    private Spinner categoryList;
    private FloatingActionButton btnSave;
    private GridView gridView;
    private Button btnUploadImage;
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result -> {
                if(result != null) {
                    images.add(new ImageUploadDTO(result));
                    gridView.setAdapter(new ImagesAdapter(CreatePostActivity.this,
                            R.id.upload_image,
                            images));
                }
            });

    private void initViews() {
        title = findViewById(R.id.post_title_create);
        content = findViewById(R.id.post_content_create);
        categoryList = findViewById(R.id.category_id);
        btnSave = findViewById(R.id.save_post);
        btnUploadImage = findViewById(R.id.upload_image);

        gridView = findViewById(R.id.images_layout);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            images.remove(position);
            gridView.setAdapter(new ImagesAdapter(CreatePostActivity.this,
                    R.id.upload_image,
                    images));
        });

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        initViews();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.create_post));
        }

        executorService.execute(() -> {
            try {
                HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                        getString(R.string.preference_key),
                        MODE_PRIVATE
                ));

                ILookupRepo lookupRepo = new LookupRepo(httpClient);
                ArrayList<PostCategory> categories = lookupRepo.getCategoryList();

                runOnUiThread(() -> {
                    categoryList.setAdapter(new CategoryAdapter(CreatePostActivity.this,
                            R.layout.spinner_item, categories));
                    btnSave.setEnabled(true);
                });

            } catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Toast.makeText(CreatePostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                });
            } catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(CreatePostActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });

        btnSave.setOnClickListener(v -> {
            if(validateInputs()) {
                btnSave.setEnabled(false);

                TextView categoryId = categoryList.getSelectedView().findViewById(R.id.category_id_spinner);
                CreatePostModel postModel = new CreatePostModel(
                        title.getText().toString(),
                        content.getText().toString(),
                        Integer.parseInt(categoryId.getText().toString())
                );

                executorService.execute(() -> {
                    try {
                        if(postsRepo == null) {
                            HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                                    getString(R.string.preference_key),
                                    MODE_PRIVATE
                            ));
                            postsRepo = new PostsRepo(httpClient);
                        }

                        String postID = postsRepo.createPost(postModel);

                        if(postID != null && !postID.equals(""))
                        {
                            if(images.size() > 3) {
                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Post Cannot have more than 3 images", Toast.LENGTH_SHORT).show();
                                    btnSave.setEnabled(true);
                                });
                            }
                            else {
                                // iterate over images and upload it
                                for(ImageUploadDTO img : images) {
                                    String imgAsBase64 = Base64Converter.convertFromByteArrToBase64(
                                            FileReaderHelper.readUri(CreatePostActivity.this,
                                                    img.getImage())
                                    );
                                    Log.d(TAG, "iterating over images: " + imgAsBase64);
                                    boolean result = postsRepo.uploadImagesToPost(new UploadImageModel(
                                            postID,
                                            "png",
                                            imgAsBase64
                                    ));
                                }

                                runOnUiThread(() -> {
                                    btnSave.setEnabled(true);
                                    Toast.makeText(this, "Post has been Created Successfully", Toast.LENGTH_SHORT).show();
                                    this.finish();
                                });
                            }
                        }
                        else
                        {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Post was not created due to an error", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } catch (IOException | UnAuthorizedException | JSONException e) {
                        Log.d(TAG, "onCreate: exceptions: " + e.getMessage());
                        runOnUiThread(() -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } catch (NoInternetException e) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(CreatePostActivity.this, NoInternetActivity.class);
                            startActivity(intent);
                        });
                    }
                });
            }
        });

        btnUploadImage.setOnClickListener(v -> {
            mGetContent.launch("image/*");
        });
    }
}