package com.example.ssteeve.dpd_android;

import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/1/16.
 */
public class DPDRequest {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static void makeRequest(String endpoint,
                                  @Nullable Map<String, Object> params,
                                  String  method,
                                  @Nullable String jsonBody,
                                  @Nullable Dictionary<String, Object> requestHeader,
                            @Nullable Class mappableObject, RequestCallBack requestCallBack) {

        String url = DPDConstants.sRootUrl + endpoint;
        if (params != null && params.size() > 0) {
            String paramString = new JSONObject(params).toString();
            url = url + "?" + paramString;
        }


        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("Content-Type", "application/json");
        requestBuilder.addHeader("Accept-Encoding", "gzip");

        if (DPDCredentials.getInstance().getAccessToken() != null)
            requestBuilder.addHeader("accessToken", DPDCredentials.getInstance().getAccessToken());

        if (DPDCredentials.getInstance().getSessionId() != null)
            requestBuilder.addHeader("Cookie", DPDCredentials.getInstance().getSessionId());

        if (requestHeader != null && requestHeader.size() > 0) {
            for (Enumeration<String> key = requestHeader.keys(); requestHeader.keys().hasMoreElements();) {
                requestBuilder.addHeader(key.toString(), requestHeader.get(key).toString());
            }
        }


        Request request;
        RequestBody body = RequestBody.create(JSON, jsonBody != null ? jsonBody : "");

        if (method == HTTPMethod.POST) {

            request = requestBuilder
                    .url(url)
                    .post(body)
                    .build();
        } else if(method == HTTPMethod.PUT) {
            request = requestBuilder
                    .url(url)
                    .put(body)
                    .build();
        } else if (method == HTTPMethod.DELETE) {
            request = requestBuilder
                    .url(url)
                    .delete(body)
                    .build();
        } else {
            request = requestBuilder
                    .url(url)
                    .build();
        }

        new BackendOperation(DPDConstants.sRootUrl, request, requestBuilder, requestCallBack);
    }
}