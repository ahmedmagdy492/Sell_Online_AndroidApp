package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.HttpClient;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.irepository.IChatsRepo;
import com.magdyradwan.sellonline.jsonreaders.ChatJsonReader;
import com.magdyradwan.sellonline.models.ChatModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ChatsRepo implements IChatsRepo {
    private final HttpClient httpClient;

    public ChatsRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public List<ChatModel> getChatsOfUser() throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Chat");
        ChatJsonReader chatJsonReader = new ChatJsonReader();
        return chatJsonReader.readJson(response);
    }

    public List<ChatModel> getChatBySenderIdAndReceiverId(String senderId, String receiverId) throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Chat/" + senderId + "/" + receiverId);
        ChatJsonReader chatJsonReader = new ChatJsonReader();
        return chatJsonReader.readJson(response);
    }
}
