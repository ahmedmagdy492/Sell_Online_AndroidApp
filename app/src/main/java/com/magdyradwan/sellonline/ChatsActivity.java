package com.magdyradwan.sellonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.magdyradwan.sellonline.adapters.ChatsAdapter;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IChatsRepo;
import com.magdyradwan.sellonline.models.ChatModel;
import com.magdyradwan.sellonline.repository.ChatsRepo;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatsActivity extends AppCompatActivity {

    private IChatsRepo chatsRepo = null;

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
                if(chatsRepo == null) {
                    HttpClient httpClient = new HttpClient(ChatsActivity.this,
                            getSharedPreferences(getString(R.string.preference_key), MODE_PRIVATE));
                    chatsRepo = new ChatsRepo(httpClient);
                }

                List<ChatModel> chats = chatsRepo.getChatsOfUser();

                runOnUiThread(() -> {
                    ChatsAdapter chatsAdapter = new ChatsAdapter(ChatsActivity.this, R.layout.chat_item, chats);
                    chat_list.setAdapter(chatsAdapter);

                    chat_list.setOnItemClickListener((parent, view, position, id) -> {
                        Intent intent = new Intent(ChatsActivity.this, MessagesActivity.class);
                        intent.putExtra("chatId", chats.get(position).getChatID());
                        if(chats.get(position).getReceiverID().equals(userId))
                        {
                            intent.putExtra("receiverId", chats.get(position).getSenderID());
                        }
                        else {
                            intent.putExtra("receiverId", chats.get(position).getReceiverID());
                        }
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