package com.magdyradwan.sellonline.responsemodels;

public class RegistreResponseModel {
    private String message;

    public RegistreResponseModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
