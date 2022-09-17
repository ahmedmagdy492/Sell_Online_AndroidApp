package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.PostCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoryListJsonReader implements IJsonReader<ArrayList<PostCategory>> {
    @Override
    public ArrayList<PostCategory> readJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        ArrayList<PostCategory> categories = new ArrayList<>();
        JSONArray arr = root.getJSONArray("data");

        for(int i = 0;i < arr.length(); i++) {
            PostCategory postCategory = new PostCategory();
            postCategory.setId(arr.getJSONObject(i).getInt("id"));
            postCategory.setName(arr.getJSONObject(i).getString("name"));
            categories.add(postCategory);
        }

        return categories;
    }
}
