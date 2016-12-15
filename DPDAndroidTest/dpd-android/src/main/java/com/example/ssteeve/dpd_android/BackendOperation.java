package com.example.ssteeve.dpd_android;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/15/16.
 */
public class BackendOperation {

    Request mRequest;
    RequestCallBack mRequestCallBack;
    String mRootUrl;
    Request.Builder mRequestBuilder;
    final String REFRESH_ACCESS_TOKEN_ENDPOINT = "refreshaccesstoken";
    private final String ACCESS_TOKEN_KEY = "accessToken";

    public BackendOperation(String rootUrl, Request request, Request.Builder builder, RequestCallBack requestCallBack) {
        mRequest = request;
        mRequestCallBack = requestCallBack;
        mRootUrl = rootUrl;
        mRequestBuilder = builder;
        makeCallFromRequest(request, requestCallBack);
    }

     void makeCallFromRequest(final Request request, final RequestCallBack requestCallBack) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (response.code() == ErrorCodes.EXPIRED_ACCESSTOKEN) {
                        DPDHelper.sOperationQueue.add(BackendOperation.this);
                       if (!DPDHelper.mIsRefreshingAccessToken) {
                           refreshAccessToken();
                       }
                    } else {
                        requestCallBack.onFailure(response);
                    }

                    throw new IOException("Unexpected code " + response);
                } else {
                    String jsonString = response.body().string();
                    try {
                        requestCallBack.onResponse(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void refreshAccessToken() {
        DPDHelper.mIsRefreshingAccessToken = true;

        String url = mRootUrl + REFRESH_ACCESS_TOKEN_ENDPOINT;

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("Content-Type", "application/json");
        requestBuilder.addHeader("Accept-Encoding", "gzip");
        if (DPDCredentials.getInstance().getSessionId() != null)
            requestBuilder.addHeader("cookie", DPDCredentials.getInstance().getSessionId());

        Request request = requestBuilder
                .url(url)
                .build();

        Log.d("Backend Operation", "*********************** Refreshing Access Token *********************");

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String jsonString = response.body().string();
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonString);
                        if (jsonObject.has(ACCESS_TOKEN_KEY)) {
                            DPDCredentials.getInstance().setAccessToken(jsonObject.getString("accessToken"));
                        }
                        DPDHelper.mIsRefreshingAccessToken = false;
                        clearQueue();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void clearQueue() {
        BackendOperation operation = DPDHelper.sOperationQueue.poll();
        while (operation != null) {
            if (DPDCredentials.getInstance().getAccessToken() != null) {
                operation.mRequestBuilder.removeHeader(ACCESS_TOKEN_KEY);
                operation.mRequestBuilder.addHeader(ACCESS_TOKEN_KEY, DPDCredentials.getInstance().getAccessToken());
            }

            if (operation.mRequest.method() == HTTPMethod.POST || operation.mRequest.method() == HTTPMethod.PUT) {
                operation.mRequest = operation.mRequestBuilder
                        .url(mRequest.url())
                        .post(mRequest.body())
                        .build();
            } else {
                operation.mRequest = operation.mRequestBuilder
                        .url(operation.mRequest.url())
                        .build();
            }

            operation.makeCallFromRequest(operation.mRequest, operation.mRequestCallBack);
            operation = DPDHelper.sOperationQueue.poll();
        }
    }
}
