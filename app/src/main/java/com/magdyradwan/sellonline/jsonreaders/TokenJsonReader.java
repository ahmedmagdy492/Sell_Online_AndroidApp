package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.TokenResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenJsonReader implements IJsonReader<TokenResponseModel> {
    @Override
    public TokenResponseModel readJson(String json) throws JSONException {
        JSONObject object = new JSONObject(json);
        TokenResponseModel responseModel = new TokenResponseModel();
        responseModel.setExp(object.getLong("exp"));
        responseModel.setName(object.getString("Name"));
        return responseModel;
    }
}
