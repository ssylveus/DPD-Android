package com.example.ssteeve.dpd_android;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import de.adorsys.android.securestoragelibrary.SecurePreferences;
import de.adorsys.android.securestoragelibrary.SecureStorageException;

/**
 * Created by ssteeve on 11/15/16.
 */
public class DPDHelper {

    static Queue<BackendOperation> sOperationQueue = new ConcurrentLinkedQueue<BackendOperation>();
    static boolean mIsRefreshingAccessToken = false;

    static void saveObject(String value, String key) {

        try {
            SecurePreferences.setValue(DPDClient.getInstance().getContext(), key, value);
        } catch (SecureStorageException e) {
            e.printStackTrace();
        }
    }

    static String getSavedObject(String key) {
        return SecurePreferences.getStringValue(DPDClient.getInstance().getContext(), key, null);
    }
}
