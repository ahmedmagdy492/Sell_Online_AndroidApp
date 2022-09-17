package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.models.ChatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatJsonReader implements IJsonReader<List<ChatModel>> {

    @Override
    public List<ChatModel> ReadJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray data = root.getJSONArray("data");
        List<ChatModel> chats = new ArrayList<>();

        for(int i = 0; i < data.length(); i++)
        {
            JSONObject cur = data.getJSONObject(i);

            String chatId = cur.getString("chatID");
            String senderId = cur.getString("senderID");
            String receiverID = cur.getString("receiverID");
            String title = cur.getString("title");
            String date = cur.getString("date");
            ChatModel chat = new ChatModel(chatId, senderId, receiverID, title, date);

            chats.add(chat);
        }

        return chats;
    }
}
