package com.magdyradwan.sellonline.models;

import com.magdyradwan.sellonline.IJsonConvertable;

public class ChangePasswordModel implements IJsonConvertable {
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    public ChangePasswordModel(String currentPassword, String newPassword, String confirmNewPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    @Override
    public String convertToJson() {
        StringBuilder builder = new StringBuilder("{\"currentPassword\":\"");
        builder.append(currentPassword).append("\",")
                .append("\"newPassword\":\"").append(newPassword).append("\",")
                .append("\"confirmNewPassword\":\"").append(confirmNewPassword)
                .append("\"}");
        return builder.toString();
    }
}
