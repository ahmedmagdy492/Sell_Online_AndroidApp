package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostImageJSONReader implements IJsonReader<ArrayList<PostImageResponseModel>> {
    @Override
    public ArrayList<PostImageResponseModel> readJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray arr = root.getJSONArray("data");
        ArrayList<PostImageResponseModel> postImages = new ArrayList<>();

        for(int i = 0;i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            PostImageResponseModel postImageResponseModel = new PostImageResponseModel();
            postImageResponseModel.setId(obj.getString("id"));
            postImageResponseModel.setPostID(obj.getString("postID"));
            postImageResponseModel.setImageURL(obj.getString("imageURL"));
            postImages.add(postImageResponseModel);
        }

        return postImages;
    }
}
