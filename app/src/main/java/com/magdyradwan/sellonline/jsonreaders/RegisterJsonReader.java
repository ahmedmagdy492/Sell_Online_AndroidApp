package com.magdyradwan.sellonline.jsonreaders;

import android.util.Log;

import com.magdyradwan.sellonline.responsemodels.RegistreResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterJsonReader implements IJsonReader<RegistreResponseModel> {
    @Override
    public RegistreResponseModel readJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        return new RegistreResponseModel(root.getString("message"));
    }
}
