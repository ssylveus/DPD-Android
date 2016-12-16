package com.example.ssteeve.dpd_android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/2/16.
 */
public interface RequestCallBack {
     void onResponse(String jsonString) throws Exception;
     void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e);
}
