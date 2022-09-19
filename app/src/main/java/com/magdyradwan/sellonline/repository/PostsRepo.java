package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.SearchActivity;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.irepository.IPostsRepo;
import com.magdyradwan.sellonline.jsonreaders.CreatePostJsonReader;
import com.magdyradwan.sellonline.jsonreaders.PostDetailsJsonReader;
import com.magdyradwan.sellonline.jsonreaders.PostImageJSONReader;
import com.magdyradwan.sellonline.jsonreaders.PostsJsonReader;
import com.magdyradwan.sellonline.models.CreatePostModel;
import com.magdyradwan.sellonline.models.UploadImageModel;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;
import com.magdyradwan.sellonline.responsemodels.PostResponseModel;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class PostsRepo implements IPostsRepo {

    private final HttpClient httpClient;

    public PostsRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public PostResponseModel getPostDetails(String postId) throws IOException, UnAuthorizedException, JSONException{
        String response = httpClient.getRequest("Posts/" + postId);
        PostDetailsJsonReader postsJsonReader = new PostDetailsJsonReader();
        return postsJsonReader.readJson(response).get(0);
    }

    public void addViewToPost(String postId) throws IOException, UnAuthorizedException {
        httpClient.postRequest("Posts/Views/Add?postId=" + postId, "");
    }

    public ArrayList<PostImageResponseModel> getImagesOfPost(String postID) throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Posts/Images?postID=" + postID);
        PostImageJSONReader postImageJSONReader = new PostImageJSONReader();
        return postImageJSONReader.readJson(response);
    }

    public ArrayList<PostResponseModel> getTrendingPosts(int pageNo, int pageSize) throws IOException, UnAuthorizedException, JSONException {
        String response =
                httpClient.getRequest("Posts/Trending?pageNo=" + pageNo + "&pageSize=" + pageSize);
        PostsJsonReader postsJsonReader = new PostsJsonReader();
        return postsJsonReader.readJson(response);
    }

    public ArrayList<PostResponseModel> getMyPosts(int pageNo, int pageSize) throws JSONException, IOException, UnAuthorizedException {
        String response = httpClient.getRequest("Posts/MyPosts?pageNo=" + pageNo + "&pageSize=" + pageSize);
        PostsJsonReader postsJsonReader = new PostsJsonReader();
        return postsJsonReader.readJson(response);
    }

    public String createPost(CreatePostModel model) throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.postRequest("Posts", model.convertToJson());
        CreatePostJsonReader createPostJsonReader = new CreatePostJsonReader();
        return createPostJsonReader.readJson(response).getPostID();
    }

    public boolean uploadImagesToPost(UploadImageModel model) throws IOException, UnAuthorizedException {
        String json = model.convertToJson();
        httpClient.postRequest("Posts/Images/Upload", json);
        return true;
    }

    public ArrayList<PostResponseModel> getPostsByCategoryID(long categoryId, int pageNo, int pageSize) throws IOException, UnAuthorizedException, JSONException {
        String json = httpClient.getRequest("Posts/Category?categoryId=" + categoryId + "&pageNo=" + pageNo + "&pageSize=" + pageSize);
        PostsJsonReader postsJsonReader = new PostsJsonReader();
        return postsJsonReader.readJson(json);
    }

    public ArrayList<PostResponseModel> searchPosts(String query) throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Posts/Search?query=" + query);
        PostsJsonReader postsJsonReader = new PostsJsonReader();
        return postsJsonReader.readJson(response);
    }
}
