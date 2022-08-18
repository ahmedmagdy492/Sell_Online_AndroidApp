package com.magdyradwan.sellonline.dto;

import com.magdyradwan.sellonline.IJsonConvertable;

public class MessageDTO implements IJsonConvertable {
    private String content;
    private String sentDate;
    private String chatID;

    public MessageDTO(String content, String sentDate, String chatID) {
        this.content = content;
        this.sentDate = sentDate;
        this.chatID = chatID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSentDate() {
        return sentDate;
    }

    public void setSentDate(String sentDate) {
        this.sentDate = sentDate;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    @Override
    public String convertToJson() {
        StringBuilder stringBuilder = new StringBuilder("{\"content\":\"");
        stringBuilder.append(content).append("\",\"sentDate\":\"");
        stringBuilder.append(sentDate).append("\",\"chatID\":\"");
        stringBuilder.append(chatID).append("\"}");
        return stringBuilder.toString();
    }
}
