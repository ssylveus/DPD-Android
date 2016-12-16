package com.example.ssteeve.dpd_android;


import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/2/16.
 */
public interface MappableResponseCallBack {
    public void onResponse(List<DPDObject> response);
    public void onFailure(Call call, Response response, Exception e);
}
