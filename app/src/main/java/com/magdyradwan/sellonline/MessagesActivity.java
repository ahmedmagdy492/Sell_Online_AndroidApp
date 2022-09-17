package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaTimestamp;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.magdyradwan.sellonline.dto.MessageDTO;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.MessagesJsonReader;
import com.magdyradwan.sellonline.models.MessageModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessagesActivity extends AppCompatActivity {

    public List<MessageModel> getMessagesOfChat(String chatID) throws IOException, NoInternetException, UnAuthorizedException, JSONException {
        HttpClient httpClient = new HttpClient(MessagesActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        String response = httpClient.getRequest("Message?chatId="+chatID);
        MessagesJsonReader messagesJsonReader = new MessagesJsonReader();
        return messagesJsonReader.ReadJson(response);
    }

    public void sendMessage(MessageDTO model) throws IOException, NoInternetException, UnAuthorizedException {
        HttpClient httpClient = new HttpClient(MessagesActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        httpClient.postRequest("Message",
                model.convertToJson());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        Button btnSend = findViewById(R.id.send_btn);
        EditText messageContent = findViewById(R.id.sendMessage);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        String receiverId = intent.getStringExtra("receiverId");
        String chatID = intent.getStringExtra("chatId");
        String userId = getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE).getString("userId", "");

        RecyclerView messagesList = findViewById(R.id.messages);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() -> {
            try {
                List<MessageModel> messages = getMessagesOfChat(chatID);

                // TODO: add messages to recycler view
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
                        sendMessage(messageDTO);
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