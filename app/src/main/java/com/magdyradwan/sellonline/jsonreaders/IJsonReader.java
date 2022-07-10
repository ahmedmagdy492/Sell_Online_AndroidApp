package com.magdyradwan.sellonline.jsonreaders;

import org.json.JSONException;

public interface IJsonReader<T> {
    T ReadJson(String json) throws JSONException;
}
