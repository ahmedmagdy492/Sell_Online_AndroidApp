package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.magdyradwan.sellonline.adapters.MessageRecyclerAdapter;
import com.magdyradwan.sellonline.dto.MessageDTO;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IMessagesRepo;
import com.magdyradwan.sellonline.jsonreaders.MessagesJsonReader;
import com.magdyradwan.sellonline.models.MessageModel;
import com.magdyradwan.sellonline.repository.MessagesRepo;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessagesActivity extends AppCompatActivity {

    private IMessagesRepo messagesRepo = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        RecyclerView messagesList = findViewById(R.id.messages);
        Intent intent = getIntent();
        String chatID = intent.getStringExtra("chatId").equals("") ? null : intent.getStringExtra("chatId");
        String userId = getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE).getString("userId", "");

        swipeRefreshLayout = findViewById(R.id.refersher);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    if(messagesRepo == null)
                    {
                        HttpClient httpClient = new HttpClient(MessagesActivity.this,
                                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

                        messagesRepo = new MessagesRepo(httpClient);
                    }

                    List<MessageModel> messages = messagesRepo.getMessagesOfChat(chatID);

                    runOnUiThread(() -> {
                        messagesList.setAdapter(new MessageRecyclerAdapter(MessagesActivity.this, messages, userId));
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagesActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        messagesList.setLayoutManager(linearLayoutManager);
                        messagesList.scrollBy(0, 10000);
                        swipeRefreshLayout.setRefreshing(false);
                    });
                }
                catch (IOException | UnAuthorizedException | JSONException e) {
                    runOnUiThread(() -> Toast.makeText(MessagesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }
                catch (NoInternetException e) {
                    runOnUiThread(() -> {
                        Intent noInternetIntent = new Intent(MessagesActivity.this, NoInternetActivity.class);
                        startActivity(noInternetIntent);
                    });
                }
            });
        });

        Button btnSend = findViewById(R.id.send_btn);
        EditText messageContent = findViewById(R.id.sendMessage);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String receiverId = intent.getStringExtra("receiverId");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            try {
                if(messagesRepo == null) {
                    HttpClient httpClient = new HttpClient(MessagesActivity.this,
                            getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));
                    messagesRepo = new MessagesRepo(httpClient);
                }

                List<MessageModel> messages = messagesRepo.getMessagesOfChat(chatID);

                // add messages to recycler view
                runOnUiThread(() -> {
                    messagesList.setAdapter(new MessageRecyclerAdapter(MessagesActivity.this, messages, userId));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagesActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    messagesList.setLayoutManager(linearLayoutManager);
                    messagesList.scrollBy(0, 10000);
                });
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> {
                    Log.d("TAG", e.getStackTrace()[0].getMethodName() + ": threw execption " + e.getClass().getName());
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
            catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent noInternetIntent = new Intent(MessagesActivity.this, NoInternetActivity.class);
                    startActivity(noInternetIntent);
                });
            }
        });

        btnSend.setOnClickListener(v -> {
            if(!messageContent.getText().toString().equals(""))
            {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setMessage(messageContent.getText().toString());
                messageDTO.setSenderId(userId);
                messageDTO.setRecieverId(receiverId);
                messageDTO.setChatID(chatID);

                executorService.execute(() -> {
                    try {
                        if(messagesRepo == null)
                        {
                            HttpClient httpClient = new HttpClient(MessagesActivity.this,
                                    getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));
                            messagesRepo = new MessagesRepo(httpClient);
                        }

                        messagesRepo.sendMessage(messageDTO);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Your Message has been sent", Toast.LENGTH_SHORT).show();
                            messageContent.setText("");
                            messagesList.scrollBy(0, 10000);
                        });
                    }
                    catch (IOException | UnAuthorizedException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                    catch (NoInternetException e) {
                        runOnUiThread(() -> {
                            Intent noInternetIntent = new Intent(MessagesActivity.this, NoInternetActivity.class);
                            startActivity(noInternetIntent);
                        });
                    }
                });
            }
        });
    }
}