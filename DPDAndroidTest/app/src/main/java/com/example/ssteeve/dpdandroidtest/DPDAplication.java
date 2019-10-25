package com.example.ssteeve.dpdandroidtest;

import android.app.Application;

import com.example.ssteeve.dpd_android.DPDClient;

/**
 * Created by ssteeve on 12/14/16.
 */

public class DPDAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DPDClient.newInstance(getApplicationContext(), "http://localhost:2403/");
    }
}
