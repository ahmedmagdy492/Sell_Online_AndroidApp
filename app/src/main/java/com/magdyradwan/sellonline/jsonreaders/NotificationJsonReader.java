package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.NotificationResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationJsonReader implements IJsonReader<List<NotificationResponseModel>> {
    @Override
    public List<NotificationResponseModel> readJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        List<NotificationResponseModel> notifications = new ArrayList<>();
        JSONArray arr = root.getJSONArray("data");

        for(int i = 0;i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            NotificationResponseModel notificationResponseModel = new NotificationResponseModel(
                    obj.getString("notificationID"),
                    obj.getString("title"),
                    obj.getString("content"),
                    obj.getString("userID"),
                    obj.getString("notificationDate")
            );
            notifications.add(notificationResponseModel);
        }

        return notifications;
    }
}
