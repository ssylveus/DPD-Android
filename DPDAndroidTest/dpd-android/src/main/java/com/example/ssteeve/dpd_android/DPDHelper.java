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

    static void saveObject(String value, String key) {

        try {
            String encryptedValue = DPDClient.getEnCryptor().encryptText(key, value);
            if (encryptedValue != null) {
                saveToSharedPreference(encryptedValue, key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void saveToSharedPreference(String value, String key) {
        SharedPreferences.Editor editor = DPDClient.getInstance().getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }


    static String getSavedObj(String key) {
        if (getObjFromSharedPreference(key) != null) {
            String encryptedValue = getObjFromSharedPreference(key);

            try {
                String decryptedValue = DPDClient.getDeCryptor().decryptData(key, encryptedValue, DPDClient.getEnCryptor().getIv(key));
                return decryptedValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    static String getObjFromSharedPreference(String key) {
        if (DPDClient.getInstance().getSharedPreferences() != null && DPDClient.getInstance().getSharedPreferences().contains(key)) {
            return DPDClient.getInstance().getSharedPreferences().getString(key, "");

        }

        return null;
    }
}
