package com.magdyradwan.sellonline.irepository;

import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.responsemodels.PostCategory;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public interface ILookupRepo {
    ArrayList<PostCategory> getCategoryList() throws IOException, UnAuthorizedException, JSONException;
}
