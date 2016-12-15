package com.example.ssteeve.dpd_android;

import android.content.SharedPreferences;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ssteeve on 11/15/16.
 */
public class DPDHelper {
    static Queue<BackendOperation> sOperationQueue = new ConcurrentLinkedQueue<BackendOperation>();
    static boolean mIsRefreshingAccessToken = false;

    static void saveObjToSharedPreference(String value, String key) {
        SharedPreferences.Editor editor = DPDClient.getInstance().getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    static String getObjFromSharedPreference(String key) {
        if (DPDClient.getInstance().getSharedPreferences() != null && DPDClient.getInstance().getSharedPreferences().contains(key)) {
            return DPDClient.getInstance().getSharedPreferences().getString(key, "");
        }

        return null;
    }
}
