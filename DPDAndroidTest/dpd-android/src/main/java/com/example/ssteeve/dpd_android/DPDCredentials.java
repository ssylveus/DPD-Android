package com.example.ssteeve.dpd_android;

import android.content.SharedPreferences;

/**
 * Created by ssteeve on 12/12/16.
 */
public class DPDCredentials {
    private static DPDCredentials ourInstance = new DPDCredentials();

    public static DPDCredentials getInstance() {
        return ourInstance;
    }

    private String mAccessToken;
    private String mSessionId;
    private String mInstallationId;
    private String mSessionToken;

    private DPDCredentials() {
    }

    public String getAccessToken() {
        if (mAccessToken == null)
            return DPDHelper.getSavedObj(DPDConstants.ACESS_TOKEN_SHARED_PREFERENCE_KEY);

        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
        DPDHelper.saveObject(accessToken, DPDConstants.ACESS_TOKEN_SHARED_PREFERENCE_KEY);
    }

    public String getSessionId() {
        if (mSessionId == null)
            return DPDHelper.getSavedObj(DPDConstants.SESSION_ID_SHARED_PREFERENCE_KEY);

        return mSessionId;
    }

    public void setSessionId(String sessionId) {
        mSessionId = "sid=" + sessionId;
        DPDHelper.saveObject(mSessionId, DPDConstants.SESSION_ID_SHARED_PREFERENCE_KEY);
    }

    public String getInstallationId() {
        if (mInstallationId == null)
            return DPDHelper.getSavedObj(DPDConstants.INSTALLATION_ID_SHARED_PREFERENCE_KEY);

        return mInstallationId;
    }

    public void setInstallationId(String installationId) {
        mInstallationId = installationId;
        DPDHelper.saveObject(installationId, DPDConstants.INSTALLATION_ID_SHARED_PREFERENCE_KEY);
    }

    public String getSessionToken() {
        if (mSessionToken == null)
            return DPDHelper.getSavedObj(DPDConstants.SESSION_TOKEN_SHARED_PREFERENCE_KEY);

        return mSessionToken;

    }

    public void setSessionToken(String sessionToken) {
        mSessionToken = sessionToken;
        DPDHelper.saveObject(sessionToken, DPDConstants.SESSION_TOKEN_SHARED_PREFERENCE_KEY);
    }

    public static void clear() {
        SharedPreferences.Editor editor = DPDClient.getInstance().getSharedPreferences().edit();
        editor.remove(DPDConstants.ACESS_TOKEN_SHARED_PREFERENCE_KEY);
        editor.remove(DPDConstants.SESSION_ID_SHARED_PREFERENCE_KEY);
        editor.remove(DPDConstants.INSTALLATION_ID_SHARED_PREFERENCE_KEY);
        editor.remove(DPDConstants.SESSION_TOKEN_SHARED_PREFERENCE_KEY);
        editor.apply();
    }
}
