package com.example.ssteeve.dpd_android;

/**
 * Created by ssteeve on 11/2/16.
 */
public class DPDConstants {

    protected static String sRootUrl = "";
    protected static boolean sSupportAccessToken;
    protected static String sAccessTokenEndPoint;
    protected static boolean sSupportRefreshToken;
    protected static String sRefreshTokenEndPoint;
    protected static Integer sExpiredAccessTokenErrorCode;

    public static final String SHARED_PREFS_USER_KEY = "CurrentUser";
    public static final String ACESS_TOKEN_SHARED_PREFERENCE_KEY = "accessToken";
    public static final String SESSION_ID_SHARED_PREFERENCE_KEY = "sessionId";
    public static final String SESSION_TOKEN_SHARED_PREFERENCE_KEY = "sessionToken";
    public static final String INSTALLATION_ID_SHARED_PREFERENCE_KEY = "installationId";
}
