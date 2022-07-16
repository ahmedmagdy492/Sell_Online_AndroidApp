package com.magdyradwan.sellonline.responsemodels;

public class PostView {
    private long postViewID;
    private String postID;
    private String viewerID;

    public long getPostViewID() {
        return postViewID;
    }

    public void setPostViewID(long postViewID) {
        this.postViewID = postViewID;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getViewerID() {
        return viewerID;
    }

    public void setViewerID(String viewerID) {
        this.viewerID = viewerID;
    }
}
