package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.responsemodels.ProfileResponseModel;

import org.json.JSONException;

import java.io.IOException;

public interface IUsersRepo {
    ProfileResponseModel getUserProfile() throws IOException, UnAuthorizedException, JSONException;
}
