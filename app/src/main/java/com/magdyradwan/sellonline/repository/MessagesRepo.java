package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.MessagesActivity;
import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.dto.MessageDTO;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.irepository.IMessagesRepo;
import com.magdyradwan.sellonline.jsonreaders.MessagesJsonReader;
import com.magdyradwan.sellonline.models.MessageModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MessagesRepo implements IMessagesRepo {
    private final HttpClient httpClient;

    public MessagesRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<MessageModel> getMessagesOfChat(String chatID) throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Message?chatId="+chatID);
        MessagesJsonReader messagesJsonReader = new MessagesJsonReader();
        return messagesJsonReader.readJson(response);
    }

    public void sendMessage(MessageDTO model) throws IOException, UnAuthorizedException {
        httpClient.postRequest("Message",
                model.convertToJson());
    }
}
