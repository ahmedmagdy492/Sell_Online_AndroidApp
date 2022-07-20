package com.magdyradwan.sellonline;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.magdyradwan.sellonline.dto.ImageUploadDTO;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.helpers.FileReaderHelper;
import com.magdyradwan.sellonline.jsonreaders.CategoryListJsonReader;
import com.magdyradwan.sellonline.jsonreaders.CreatePostJsonReader;
import com.magdyradwan.sellonline.models.CreatePostModel;
import com.magdyradwan.sellonline.models.UploadImageModel;
import com.magdyradwan.sellonline.responsemodels.PostCategory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreatePostActivity extends AppCompatActivity {

    private static final String TAG = "CreatePostActivity";

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

    private ArrayList<PostCategory> getCategoryList() throws IOException, UnAuthorizedException, JSONException, NoInternetException {
        HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                getString(R.string.preference_key),
                MODE_PRIVATE
        ));

        String response = httpClient.getRequest("Lookups/Categories");
        CategoryListJsonReader categoryListJsonReader = new CategoryListJsonReader();
        return categoryListJsonReader.ReadJson(response);
    }

    private String createPost() throws IOException, UnAuthorizedException, JSONException, NoInternetException {
        TextView categoryId = categoryList.getSelectedView().findViewById(R.id.category_id_spinner);
        CreatePostModel postModel = new CreatePostModel(
                title.getText().toString(),
                content.getText().toString(),
                Integer.parseInt(categoryId.getText().toString())
                );
        HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                getString(R.string.preference_key),
                MODE_PRIVATE
        ));

        String response = httpClient.postRequest("Posts", postModel.convertToJson());
        CreatePostJsonReader createPostJsonReader = new CreatePostJsonReader();
        return createPostJsonReader.ReadJson(response).getPostID();
    }

    private boolean uploadImagesToPost(UploadImageModel model) throws IOException, UnAuthorizedException, NoInternetException {
        HttpClient httpClient = new HttpClient(CreatePostActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        String json = model.convertToJson();
        httpClient.postRequest("Posts/Images/Upload", json);
        return true;
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
                ArrayList<PostCategory> categories = getCategoryList();

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
                executorService.execute(() -> {
                    try {
                        String postID = createPost();

                        if(postID != null && !postID.equals(""))
                        {
                            if(images.size() > 3) {
                                runOnUiThread(() -> {
                                    Toast.makeText(this, "Post Cannot have more than 3 images", Toast.LENGTH_SHORT).show();
                                });
                            }
                            else {
                                // TODO: iterate over images and upload it
                                for(ImageUploadDTO img : images) {
                                    String imgAsBase64 = Base64Converter.convertFromByteArrToBase64(
                                            FileReaderHelper.readUri(CreatePostActivity.this,
                                                    img.getImage())
                                    );
                                    Log.d(TAG, "iterating over images: " + imgAsBase64);
                                    boolean result = uploadImagesToPost(new UploadImageModel(
                                            postID,
                                            "png",
                                            imgAsBase64
                                    ));
                                }

                                runOnUiThread(() -> {
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