package com.magdyradwan.sellonline.repository;

import com.magdyradwan.sellonline.HttpClient;
import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.exceptions.NoInternetException;
import com.magdyradwan.sellonline.exceptions.UnAuthorizedException;
import com.magdyradwan.sellonline.irepository.ILookupRepo;
import com.magdyradwan.sellonline.jsonreaders.CategoryListJsonReader;
import com.magdyradwan.sellonline.responsemodels.PostCategory;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class LookupRepo implements ILookupRepo {
    private final HttpClient httpClient;

    public LookupRepo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ArrayList<PostCategory> getCategoryList() throws IOException, UnAuthorizedException, JSONException {
        String response = httpClient.getRequest("Lookups/Categories");
        CategoryListJsonReader categoryListJsonReader = new CategoryListJsonReader();
        return categoryListJsonReader.readJson(response);
    }
}
