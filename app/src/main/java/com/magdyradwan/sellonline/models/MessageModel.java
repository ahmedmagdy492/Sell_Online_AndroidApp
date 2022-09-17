package com.magdyradwan.sellonline.models;

public class MessageModel {
    private String id;
    private String content;
    private String sentDate;
    private String chatID;
    private String senderID;
    private String receiverID;
    private boolean seen;

    public MessageModel(String id, String content, String sentDate, String chatID, String senderID, String receiverID, boolean seen) {
        this.id = id;
        this.content = content;
        this.sentDate = sentDate;
        this.chatID = chatID;
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.seen = seen;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
