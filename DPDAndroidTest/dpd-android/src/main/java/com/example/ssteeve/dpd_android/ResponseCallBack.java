package com.example.ssteeve.dpd_android;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/2/16.
 */
public interface ResponseCallBack {
    public void onResponse(String response);
    public void onFailure(Call call, Response response, Exception e);
}
