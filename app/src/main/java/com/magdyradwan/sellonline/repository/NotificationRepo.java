package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.HttpClient;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.irepository.INotificationRepo;
import com.magdyradwan.sellonline.jsonreaders.NotificationJsonReader;
import com.magdyradwan.sellonline.responsemodels.NotificationResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class NotificationRepo implements INotificationRepo {
    private final HttpClient httpClient;

    public NotificationRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<NotificationResponseModel> getMyNotifications () throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Notification");
        NotificationJsonReader notificationJsonReader = new NotificationJsonReader();
        return notificationJsonReader.readJson(response);
    }
}
