package com.magdyradwan.sellonline.viewmodels;

public class RegisterViewModel implements IJsonConvertable {
    private String displayName;
    private String email;
    private String password;
    private String country;
    private String city;
    private String district;
    private String phoneNumber1;
    private String profileImage;
    private String imageType;

    public RegisterViewModel(String displayName, String email, String password, String country, String city, String district, String phoneNumber1, String profileImage, String imageType) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.country = country;
        this.city = city;
        this.district = district;
        this.phoneNumber1 = phoneNumber1;
        this.profileImage = profileImage;
        this.imageType = imageType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Override
    public String convertToJson() {
        StringBuilder builder = new StringBuilder("{\"email\": \"")
                .append(email).append("\",\"displayName\": \"")
                .append(displayName).append("\",\"password\": \"")
                .append(password).append("\",\"country\": \"")
                .append(country).append("\",\"city\": \"")
                .append(city).append("\",\"district\": \"")
                .append(district).append("\",\"imageType\": \"")
                .append(imageType).append("\",\"phoneNumber1\": \"")
                .append(phoneNumber1).append("\",\"profileImage\": \"")
                .append(profileImage).append("\"}");
        return builder.toString();
    }
}
