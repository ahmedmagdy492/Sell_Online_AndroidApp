package com.magdyradwan.sellonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.magdyradwan.sellonline.adapters.PostImageAdapter;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.irepository.IChatsRepo;
import com.magdyradwan.sellonline.irepository.IPostsRepo;
import com.magdyradwan.sellonline.jsonreaders.PostDetailsJsonReader;
import com.magdyradwan.sellonline.jsonreaders.PostImageJSONReader;
import com.magdyradwan.sellonline.models.ChatModel;
import com.magdyradwan.sellonline.repository.ChatsRepo;
import com.magdyradwan.sellonline.repository.PostsRepo;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailsActivity";
    private GridView images_list;
    private String userId;
    private IPostsRepo postsRepo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        images_list = findViewById(R.id.images_list);

        ExecutorService service = Executors.newFixedThreadPool(3);
        String postId = getIntent().getStringExtra("post_id");
        userId = getIntent().getStringExtra("user_id");

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            // Issue a web request to get the post details
            service.execute(() -> {
                try {
                    if(postsRepo == null)
                    {
                        HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                                getString(R.string.preference_key),
                                MODE_PRIVATE
                        ));
                        postsRepo = new PostsRepo(httpClient);
                    }

                    PostResponseModel post = postsRepo.getPostDetails(postId);

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
                if(postsRepo == null)
                {
                    HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                            getString(R.string.preference_key),
                            MODE_PRIVATE
                    ));
                    postsRepo = new PostsRepo(httpClient);
                }

                postsRepo.addViewToPost(postId);
            }
            catch (IOException | UnAuthorizedException e) {

                runOnUiThread(() -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(PostDetailsActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });

        service.execute(() -> {
            try {
                if(postsRepo == null)
                {
                    HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                            getString(R.string.preference_key),
                            MODE_PRIVATE
                    ));
                    postsRepo = new PostsRepo(httpClient);
                }

                ArrayList<PostImageResponseModel> postImages = postsRepo.getImagesOfPost(postId);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String currentUser = getSharedPreferences(getString(R.string.preference_key),
                MODE_PRIVATE).getString("userId", "");
        if(!userId.equals(currentUser)) {
            getMenuInflater().inflate(R.menu.post_details_menu, menu);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.send_msg) {
            // TODO: check if there is already a chat between this sender id and receiver id

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    HttpClient httpClient = new HttpClient(this, getSharedPreferences(
                            getString(R.string.preference_key),
                            MODE_PRIVATE
                    ));

                    String currentUser = getSharedPreferences(getString(R.string.preference_key),
                            MODE_PRIVATE).getString("userId", "");
                    IChatsRepo chatsRepo = new ChatsRepo(httpClient);

                    List<ChatModel> chat = chatsRepo.getChatBySenderIdAndReceiverId(currentUser, userId);

                    runOnUiThread(() -> {
                        Intent intent = new Intent(this, MessagesActivity.class);
                        intent.putExtra("receiverId", userId);
                        if(chat.size() == 0) {
                            intent.putExtra("chatId", "");
                        }
                        else {
                            intent.putExtra("chatId", chat.get(0).getChatID());
                        }
                        startActivity(intent);
                    });
                }
                catch (IOException | JSONException | UnAuthorizedException e) {
                    Log.d(TAG, "onOptionsItemSelected: " + e.getMessage());
                    runOnUiThread(() -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
                catch (NoInternetException e) {
                    runOnUiThread(() -> {
                        Intent intent = new Intent(PostDetailsActivity.this, NoInternetActivity.class);
                        startActivity(intent);
                    });
                }
            });
        }
        return true;
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