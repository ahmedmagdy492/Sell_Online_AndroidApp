package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.models.ChatModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface IChatsRepo {
    List<ChatModel> getChatsOfUser() throws IOException, UnAuthorizedException, JSONException;
    List<ChatModel> getChatBySenderIdAndReceiverId(String senderId, String receiverId) throws IOException, UnAuthorizedException, JSONException;
}
