package com.example.ssteeve.dpd_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.securepreferences.SecurePreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ssteeve on 12/12/16.
 */
public class DPDClient {
    private static DPDClient ourInstance = new DPDClient();

    public static DPDClient getInstance() {
        return ourInstance;
    }

    private static Context sContext;
    private static SharedPreferences sSharedPreferences;

    private DPDClient() {
    }

    public DPDClient(Context context, String rootUrl) {
        sContext = context;
        DPDConstants.sRootUrl = rootUrl;
    }

    public static DPDClient newInstance(Context context, String rootUrl) {
        DPDClient client = new DPDClient(context, rootUrl);
        getInstance().initializeSecurePreference();
        return client;
    }

    public Context getContext() {
        return sContext;
    }

    public void initializeSecurePreference() {
        sSharedPreferences = new SecurePreferences(getContext());

    }



    public static SharedPreferences getSharedPreferences() {
        if (sSharedPreferences == null)
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getInstance().getContext());

        return sSharedPreferences;
    }
}
