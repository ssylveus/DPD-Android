package com.example.ssteeve.dpd_android;

import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by ssteeve on 11/15/16.
 */
public class DPDHelper {
    private static final String TAG = "Encryptor";
    static Queue<BackendOperation> sOperationQueue = new ConcurrentLinkedQueue<BackendOperation>();
    static boolean mIsRefreshingAccessToken = false;

    static void saveObjToKeyStore(String value, String key) {
        try {
           DPDClient.getEnCryptor().encryptText(key, value);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | NoSuchProviderException |
                KeyStoreException | IOException | NoSuchPaddingException | InvalidKeyException e) {
            Log.e(TAG, "onClick() called with: " + e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException | SignatureException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    static String getObjFromKeyStore(String key) {
        try {
            return DPDClient.getDeCryptor().decryptData(key, DPDClient.getEnCryptor().getEncryption(), DPDClient.getEnCryptor().getIv());
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException |
                KeyStoreException | NoSuchPaddingException | NoSuchProviderException |
                IOException | InvalidKeyException e) {
            Log.e(TAG, "decryptData() called with: " + e.getMessage(), e);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return  null;
    }
}
