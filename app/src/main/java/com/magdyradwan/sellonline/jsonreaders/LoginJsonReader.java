package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.LoginResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginJsonReader implements IJsonReader<LoginResponseModel> {

    @Override
    public LoginResponseModel ReadJson(String json) throws JSONException {
        LoginResponseModel loginResponseModel = new LoginResponseModel();
        JSONObject object = new JSONObject(json);
        loginResponseModel.setMessage(object.getString("message"));
        loginResponseModel.setToken(object.getString("token"));
        loginResponseModel.setUserId(object.getString("userID"));
        return loginResponseModel;
    }
}
