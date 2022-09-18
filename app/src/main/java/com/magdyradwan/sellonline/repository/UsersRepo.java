package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.ChangePasswordActivity;
import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.helpers.HttpClient;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.irepository.IUsersRepo;
import com.magdyradwan.sellonline.jsonreaders.ProfileJsonReader;
import com.magdyradwan.sellonline.models.ChangePasswordModel;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import org.json.JSONException;

import java.io.IOException;

public class UsersRepo implements IUsersRepo {
    public final HttpClient httpClient;

    public UsersRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ProfileResponseModel getUserProfile() throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Users/Profile");
        ProfileJsonReader profileJsonReader = new ProfileJsonReader();
        return profileJsonReader.readJson(response).get(0);
    }

    public boolean changePassword(ChangePasswordModel model) throws IOException, UnAuthorizedException {
        httpClient.postRequest("Auth/ChangePassword", model.convertToJson());
        return true;
    }
}
