package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.models.CreatePostModel;
import com.magdyradwan.sellonline.models.UploadImageModel;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public interface IPostsRepo {
    PostResponseModel getPostDetails(String postId) throws IOException, UnAuthorizedException, JSONException;
    void addViewToPost(String postId) throws IOException, UnAuthorizedException;
    ArrayList<PostImageResponseModel> getImagesOfPost(String postID) throws IOException, UnAuthorizedException, JSONException;
    ArrayList<PostResponseModel> getTrendingPosts(int pageNo, int pageSize) throws IOException, UnAuthorizedException, JSONException;
    ArrayList<PostResponseModel> getMyPosts(int pageNo, int pageSize) throws JSONException, IOException, UnAuthorizedException;
    String createPost(CreatePostModel model) throws IOException, UnAuthorizedException, JSONException;
    boolean uploadImagesToPost(UploadImageModel model) throws IOException, UnAuthorizedException;
}
