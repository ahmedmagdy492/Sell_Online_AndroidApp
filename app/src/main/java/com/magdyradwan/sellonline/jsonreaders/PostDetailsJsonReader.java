package com.magdyradwan.sellonline.jsonreaders;

import com.magdyradwan.sellonline.responsemodels.PostCategory;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;
import com.magdyradwan.sellonline.responsemodels.PostView;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostDetailsJsonReader implements IJsonReader<ArrayList<PostResponseModel>> {
    @Override
    public ArrayList<PostResponseModel> ReadJson(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray data = root.getJSONArray("data");
        ArrayList<PostResponseModel> posts = new ArrayList<>();

        for(int i = 0;i < data.length(); i++) {
            JSONObject current = data.getJSONObject(i);

            PostResponseModel postResponseModel = new PostResponseModel();
            postResponseModel.setPostID(current.getString("postID"));
            postResponseModel.setTitle(current.getString("title"));
            postResponseModel.setContent(current.getString("content"));
            postResponseModel.setCreationDate(current.getString("creationDate"));
            postResponseModel.setEdited(current.getBoolean("isEdited"));
            postResponseModel.setEditDate(current.getString("editDate"));
            postResponseModel.setPostStatesStateID(current.getLong("postStatesStateID"));
            postResponseModel.setSoldDate(current.getString("soldDate"));
            postResponseModel.setUserID(current.getString("userID"));
            postResponseModel.setPostCategoryID(current.getLong("postCategoryID"));

            JSONObject user = current.getJSONObject("user");
            ProfileResponseModel profileResponseModel = new ProfileResponseModel();
            profileResponseModel.setUserId(user.getString("userID"));
            profileResponseModel.setDisplayName(user.getString("displayName"));
            profileResponseModel.setProfileImageURL(user.getString("profileImageURL"));

            postResponseModel.setUser(profileResponseModel);

            JSONObject category = current.getJSONObject("postCategory");
            PostCategory postCategory = new PostCategory();
            postCategory.setId(category.getInt("id"));
            postCategory.setName(category.getString("name"));
            postResponseModel.setPostCategory(postCategory);

            ArrayList<PostView> postViews = new ArrayList<>();
            JSONArray views = current.getJSONArray("postViews");

            for(int j = 0; j < views.length(); j++) {
                PostView postView = new PostView();
                JSONObject view = views.getJSONObject(j);
                postView.setViewerID(view.getString("viewerID"));
                postView.setPostID(view.getString("postID"));
                postViews.add(postView);
            }

            postResponseModel.setPostViews(postViews);

            posts.add(postResponseModel);
        }

        return posts;
    }
}
