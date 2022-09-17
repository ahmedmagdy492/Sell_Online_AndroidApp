package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.HttpClient;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.irepository.IAuthRepo;
import com.magdyradwan.sellonline.jsonreaders.LoginJsonReader;
import com.magdyradwan.sellonline.responsemodels.LoginResponseModel;
import com.magdyradwan.sellonline.viewmodels.LoginViewModel;

import org.json.JSONException;

import java.io.IOException;

public class AuthRepo implements IAuthRepo {
    private final HttpClient httpClient;

    public AuthRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public boolean checkForTokenValidity() throws IOException, UnAuthorizedException {
        httpClient.getRequest("Auth/IsValid");
        return true;
    }

    public LoginResponseModel login(LoginViewModel model) throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.postRequest("Auth/Login", model.convertToJson());

        LoginJsonReader loginJsonReader = new LoginJsonReader();
        return loginJsonReader.readJson(response);
    }
}
