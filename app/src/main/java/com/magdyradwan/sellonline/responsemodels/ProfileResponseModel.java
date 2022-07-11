package com.magdyradwan.sellonline.responsemodels;

import java.util.ArrayList;

public class ProfileResponseModel {
    private String displayName;
    private String email;
    private String country;
    private String city;
    private String district;
    private String userId;
    private ArrayList<PhoneNumberResponseModel> phoneNumbers;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<PhoneNumberResponseModel> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<PhoneNumberResponseModel> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
