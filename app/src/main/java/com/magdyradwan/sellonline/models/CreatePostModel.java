package com.magdyradwan.sellonline.models;

import com.magdyradwan.sellonline.viewmodels.IJsonConvertable;

public class CreatePostModel implements IJsonConvertable {

    private String title;
    private String content;
    private int categoryId;

    public CreatePostModel(String title, String content, int categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String convertToJson() {
        StringBuilder builder = new StringBuilder("{\"title\":\"")
                .append(title).append("\",")
                .append("\"content\":\"")
                .append(content).append("\",")
                .append("\"categoryId\":").append(categoryId)
                .append("}");
        return builder.toString();
    }
}
