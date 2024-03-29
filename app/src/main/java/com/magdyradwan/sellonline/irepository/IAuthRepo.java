package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.responsemodels.LoginResponseModel;
import com.magdyradwan.sellonline.responsemodels.RegistreResponseModel;
import com.magdyradwan.sellonline.viewmodels.LoginViewModel;
import com.magdyradwan.sellonline.viewmodels.RegisterViewModel;

import org.json.JSONException;

import java.io.IOException;

public interface IAuthRepo {
    boolean checkForTokenValidity() throws IOException, UnAuthorizedException;
    LoginResponseModel login(LoginViewModel model) throws IOException, UnAuthorizedException, JSONException;
    RegistreResponseModel register(RegisterViewModel model) throws IOException, UnAuthorizedException, JSONException;
}
