package com.magdyradwan.sellonline.dto;

import android.net.Uri;

public class ImageUploadDTO {
    private Uri image;

    public ImageUploadDTO(Uri image) {
        this.image = image;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
