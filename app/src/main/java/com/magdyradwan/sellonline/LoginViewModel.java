package com.magdyradwan.sellonline;

public class LoginViewModel implements IJsonConvertable {
    private String Email;
    private String Password;

    public LoginViewModel(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String convertToJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"email\":\"").append(getEmail()).append("\",");
        json.append("\"password\":\"").append(getPassword()).append("\"");
        json.append("}");
        return json.toString();
    }
}
