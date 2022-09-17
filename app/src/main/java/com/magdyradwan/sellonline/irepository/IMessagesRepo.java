package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.dto.MessageDTO;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.models.MessageModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public interface IMessagesRepo {
    List<MessageModel> getMessagesOfChat(String chatID) throws IOException, UnAuthorizedException, JSONException;
    void sendMessage(MessageDTO model) throws IOException, UnAuthorizedException;
}
