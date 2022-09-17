package com.magdyradwan.sellonline.dto;

import com.magdyradwan.sellonline.IJsonConvertable;

public class MessageDTO implements IJsonConvertable {
    private String chatID;
    private String message;
    private String senderId;
    private String recieverId;

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    @Override
    public String convertToJson() {
        StringBuilder stringBuilder = new StringBuilder("{\"message\":\"");
        stringBuilder.append(message).append("\",\"chatID\":\"");
        stringBuilder.append(chatID).append("\",\"senderId\":\"");
        stringBuilder.append(senderId).append("\",\"receiverID\":\"");
        stringBuilder.append(recieverId).append("\"}");
        return stringBuilder.toString();
    }
}
