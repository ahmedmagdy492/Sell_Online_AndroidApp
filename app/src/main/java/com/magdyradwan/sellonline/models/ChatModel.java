package com.magdyradwan.sellonline.models;

public class ChatModel {
    private String chatID;
    private String senderID;
    private String receiverID;
    private String title;
    private String date;

    public ChatModel(String chatID, String senderID, String receiverID, String title, String date) {
        this.chatID = chatID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.title = title;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }
}
