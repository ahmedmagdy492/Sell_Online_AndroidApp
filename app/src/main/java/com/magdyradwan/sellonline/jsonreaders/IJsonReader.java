package com.magdyradwan.sellonline.jsonreaders;

import org.json.JSONException;

public interface IJsonReader<T> {
    T readJson(String json) throws JSONException;
}
