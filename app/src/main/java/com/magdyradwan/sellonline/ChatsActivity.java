package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.jsonreaders.ChatJsonReader;
import com.magdyradwan.sellonline.models.ChatModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatsActivity extends AppCompatActivity {

    public List<ChatModel> getChatsOfUser(String userId) throws IOException, NoInternetException, UnAuthorizedException, JSONException {

        HttpClient httpClient = new HttpClient(ChatsActivity.this,
                getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));

        String response = httpClient.getRequest("Chats");
        ChatJsonReader chatJsonReader = new ChatJsonReader();
        return chatJsonReader.ReadJson(response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(getString(R.string.messages));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ListView chat_list = findViewById(R.id.chat_list);

        String userId = getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE).getString("userId", "");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                List<ChatModel> chats = getChatsOfUser(userId);

                runOnUiThread(() -> {
                    ChatsAdapter chatsAdapter = new ChatsAdapter(ChatsActivity.this, R.layout.chat_item, chats);
                    chat_list.setAdapter(chatsAdapter);

                    chat_list.setOnItemClickListener((parent, view, position, id) -> {
                        Intent intent = new Intent(ChatsActivity.this, MessagesActivity.class);
                        intent.putExtra("chatId", chats.get(position).getChatID());
                        intent.putExtra("receiverId", chats.get(position).getReceiverID());
                        startActivity(intent);
                    });
                });
            }
            catch (IOException | UnAuthorizedException | JSONException e) {
                runOnUiThread(() -> Toast.makeText(ChatsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            catch (NoInternetException e) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(ChatsActivity.this, NoInternetActivity.class);
                    startActivity(intent);
                });
            }
        });
    }
}