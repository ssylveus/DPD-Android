package com.example.ssteeve.dpd_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.securepreferences.SecurePreferences;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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


    private static DPDEnCryptor enCryptor;
    private static DPDDeCryptor deCryptor;

    private DPDClient() {
    }


    public DPDClient(Context context, String rootUrl,
                     boolean supportAccessToken, String accessTokenEndpoint,
                     boolean supportRefreshToken, String refreshTokenEndpoint,
                     Integer expiredAccessTokenErrorCode) {

        sContext = context;
        DPDConstants.sRootUrl = rootUrl;
        DPDConstants.sSupportAccessToken = supportAccessToken;
        DPDConstants.sAccessTokenEndPoint = accessTokenEndpoint;
        DPDConstants.sSupportRefreshToken = supportRefreshToken;
        DPDConstants.sRefreshTokenEndPoint = refreshTokenEndpoint;
        DPDConstants.sExpiredAccessTokenErrorCode = expiredAccessTokenErrorCode;
    }

    public static DPDClient newInstance(Context context, String rootUrl) {
        DPDClient client = new DPDClient(context, rootUrl, false, null, false, null, null);
        getInstance().initializeSecurePreference();
        return client;
    }

    public static DPDClient newInstance(Context context, String rootUrl,
                                        boolean supportAccessToken, String accessTokenEndpoint,
                                        boolean supportRefreshToken, String refreshTokenEndpoint, Integer expiredAccessTokenErrorCode) {

        DPDClient client = new DPDClient(context, rootUrl, supportAccessToken, accessTokenEndpoint,
                supportRefreshToken, refreshTokenEndpoint, expiredAccessTokenErrorCode);
        getInstance().initializeSecurePreference();

        DPDClient.enCryptor = new DPDEnCryptor();

        try {
            DPDClient.deCryptor = new DPDDeCryptor();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException |
                IOException e) {
            e.printStackTrace();
        }

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

    public static DPDEnCryptor getEnCryptor() {
        return enCryptor;
    }

    public static DPDDeCryptor getDeCryptor() {
        return deCryptor;
    }
}
