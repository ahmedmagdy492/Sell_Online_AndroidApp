package com.magdyradwan.sellonline.dto;

import android.net.Uri;

public class ImageUploadDTO {
    private Uri image;
    private String imageID;

    public ImageUploadDTO(Uri image) {
        this.image = image;
    }

    public Uri getImage() {
        return image;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
