package com.magdyradwan.sellonline.dto;

public class ProfileOptionDTO {
    private String title;
    private String itemName;
    private boolean isButton;

    public ProfileOptionDTO(String title, String itemName, boolean isButton) {
        this.title = title;
        this.itemName = itemName;
        this.isButton = isButton;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isButton() {
        return isButton;
    }

    public void setButton(boolean button) {
        isButton = button;
    }
}
