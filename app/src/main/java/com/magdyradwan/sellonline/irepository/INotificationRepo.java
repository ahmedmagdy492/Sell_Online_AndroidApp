package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.responsemodels.NotificationResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface INotificationRepo {
    List<NotificationResponseModel> getMyNotifications () throws IOException, UnAuthorizedException, JSONException;
}
