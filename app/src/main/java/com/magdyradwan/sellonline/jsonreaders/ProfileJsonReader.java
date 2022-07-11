package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.PhoneNumberResponseModel;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileJsonReader implements IJsonReader<ArrayList<ProfileResponseModel>> {

    @Override
    public ArrayList<ProfileResponseModel> ReadJson(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        JSONArray dataArray = object.getJSONArray("data");
        ArrayList<ProfileResponseModel> profileResponseModels = new ArrayList<>();

        for(int i = 0;i < dataArray.length(); i++) {
            JSONObject currentObject = dataArray.getJSONObject(i);

            ProfileResponseModel responseModel = new ProfileResponseModel();
            responseModel.setDisplayName(currentObject.getString("displayName"));
            responseModel.setEmail(currentObject.getString("email"));
            responseModel.setCountry(currentObject.getString("country"));
            responseModel.setCity(currentObject.getString("city"));
            responseModel.setDistrict(currentObject.getString("district"));
            responseModel.setUserId(currentObject.getString("userID"));

            ArrayList<PhoneNumberResponseModel> phoneNumbers = new ArrayList<>();
            JSONArray phoneNumbersArray = currentObject.getJSONArray("phoneNumbers");

            for(int j = 0;j < phoneNumbersArray.length(); j++) {
                JSONObject currentPhoneNumber = phoneNumbersArray.getJSONObject(j);
                PhoneNumberResponseModel phoneNumberResponseModel = new PhoneNumberResponseModel();
                phoneNumberResponseModel.setId(currentPhoneNumber.getString("id"));
                phoneNumberResponseModel.setPhoneNumber(currentPhoneNumber.getString("phoneNumber"));
                phoneNumberResponseModel.setUserId(currentPhoneNumber.getString("userID"));
                phoneNumbers.add(phoneNumberResponseModel);
            }

            responseModel.setPhoneNumbers(phoneNumbers);
            profileResponseModels.add(responseModel);
        }

        return profileResponseModels;
    }
}
