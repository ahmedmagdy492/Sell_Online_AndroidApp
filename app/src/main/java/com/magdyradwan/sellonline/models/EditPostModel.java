package com.magdyradwan.sellonline.models;

import com.magdyradwan.sellonline.viewmodels.IJsonConvertable;

public class EditPostModel implements IJsonConvertable {
    private String postID;
    private String title;
    private String content;
    private int categoryID;

    public EditPostModel(String postID, String title, String content, int categoryID) {
        this.postID = postID;
        this.title = title;
        this.content = content;
        this.categoryID = categoryID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
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

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String convertToJson() {
        StringBuilder builder = new StringBuilder("{\"postID\":\"").append(postID).append("\",")
                .append("\"title\":\"")
                .append(title).append("\",")
                .append("\"content\":\"")
                .append(content).append("\",")
                .append("\"categoryId\":").append(categoryID)
                .append("}");
        return builder.toString();
    }
}
