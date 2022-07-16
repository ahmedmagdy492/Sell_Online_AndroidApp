package com.magdyradwan.sellonline.models;

import com.magdyradwan.sellonline.IJsonConvertable;

public class UploadImageModel implements IJsonConvertable {
    private String postID;
    private String imageType;
    private String base64;

    public UploadImageModel(String postID, String imageType, String base64) {
        this.postID = postID;
        this.imageType = imageType;
        this.base64 = base64;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    @Override
    public String convertToJson() {
        StringBuilder strBuilder = new StringBuilder("{\"postID\":\"").append(postID).append("\",")
                .append("\"ImageType\":\"").append(imageType).append("\",")
                .append("\"Base64\":\"").append(base64).append("\"}");

        return strBuilder.toString();
    }
}
