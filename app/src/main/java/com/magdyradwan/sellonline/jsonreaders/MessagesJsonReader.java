package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.models.MessageModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessagesJsonReader implements IJsonReader<List<MessageModel>> {
    @Override
    public List<MessageModel> ReadJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray data = root.getJSONArray("data");
        List<MessageModel> msgs = new ArrayList<>();

        for(int i = 0;i < data.length(); i++)
        {
            JSONObject cur = data.getJSONObject(i);
            String id = cur.getString("id");
            String content = cur.getString("content");
            String sentDate = cur.getString("sentDate");
            String chatID = cur.getString("chatID");
            boolean seen = cur.getBoolean("seen");
            String senderId = cur.getString("senderID");
            String receiverId = cur.getString("receiverID");

            msgs.add(new MessageModel(id, content, sentDate, chatID, senderId, receiverId, seen));
        }
        return msgs;
    }
}
