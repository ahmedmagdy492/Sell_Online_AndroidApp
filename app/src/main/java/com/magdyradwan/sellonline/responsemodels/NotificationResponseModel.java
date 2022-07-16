package com.magdyradwan.sellonline.responsemodels;

public class NotificationResponseModel {
    private String notificationID;
    private String title;
    private String content;
    private String UserID;
    private String notificationDate;

    public NotificationResponseModel(String notificationID, String title, String content, String userID, String notificationDate) {
        this.notificationID = notificationID;
        this.title = title;
        this.content = content;
        UserID = userID;
        this.notificationDate = notificationDate;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }
}
