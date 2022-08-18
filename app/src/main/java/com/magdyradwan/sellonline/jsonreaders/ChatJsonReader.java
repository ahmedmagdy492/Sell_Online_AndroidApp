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
            ChatModel chat = new ChatModel(chatId, senderId, receiverID, null, null);

            if(!cur.isNull("senderName"))
            {
                String senderName = cur.getString("senderName");
                chat.setSenderName(senderName);
            }

            if(!cur.isNull("receiverName"))
            {
                String receiverName = cur.getString("receiverName");
                chat.setReceiverName(receiverName);
            }

            chats.add(chat);
        }

        return chats;
    }
}
