package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.CreatePostResponseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePostJsonReader implements IJsonReader<CreatePostResponseModel> {
    @Override
    public CreatePostResponseModel readJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        CreatePostResponseModel createPostResponseModel = new CreatePostResponseModel();
        createPostResponseModel.setMessage(root.getString("message"));
        createPostResponseModel.setPostID(root.getString("postID"));
        return createPostResponseModel;
    }
}
