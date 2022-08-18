package com.magdyradwan.sellonline.models;

public class MessageModel {
    private String id;
    private String content;
    private String sentDate;
    private String chatID;
    private boolean seen;

    public MessageModel(String id, String content, String sentDate, String chatID, boolean seen) {
        this.id = id;
        this.content = content;
        this.sentDate = sentDate;
        this.chatID = chatID;
        this.seen = seen;
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
