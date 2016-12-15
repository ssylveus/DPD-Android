package com.example.ssteeve.dpd_android;

import android.app.Application;

/**
 * Created by ssteeve on 12/13/16.
 */

public class DPDAplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DPDClient.newInstance(getApplicationContext(), "https://usicians.com/");
    }
}
