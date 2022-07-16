package com.magdyradwan.sellonline.responsemodels;

import java.util.ArrayList;
import java.util.Date;

public class PostResponseModel {
    private String postID;
    private String title;
    private String content;
    private String creationDate;
    private boolean isEdited;
    private String editDate;
    private long postStatesStateID;
    private String soldDate;
    private String userID;
    private long postCategoryID;
    private ProfileResponseModel user;
    private PostCategory postCategory;
    private ArrayList<PostView> postViews;

    public ArrayList<PostView> getPostViews() {
        return postViews;
    }

    public void setPostViews(ArrayList<PostView> postViews) {
        this.postViews = postViews;
    }

    public PostCategory getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(PostCategory postCategory) {
        this.postCategory = postCategory;
    }

    public ProfileResponseModel getUser() {
        return user;
    }

    public void setUser(ProfileResponseModel user) {
        this.user = user;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public String getEditDate() {
        return editDate;
    }

    public void setEditDate(String editDate) {
        this.editDate = editDate;
    }

    public long getPostStatesStateID() {
        return postStatesStateID;
    }

    public void setPostStatesStateID(long postStatesStateID) {
        this.postStatesStateID = postStatesStateID;
    }

    public String getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(String soldDate) {
        this.soldDate = soldDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getPostCategoryID() {
        return postCategoryID;
    }

    public void setPostCategoryID(long postCategoryID) {
        this.postCategoryID = postCategoryID;
    }
}
