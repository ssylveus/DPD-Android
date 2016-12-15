package com.example.ssteeve.dpd_android;

import org.json.JSONException;

import okhttp3.Response;

/**
 * Created by ssteeve on 11/2/16.
 */
public interface RequestCallBack {
    public void onResponse(String jsonString) throws JSONException;
    public void onFailure(Response response);
}
