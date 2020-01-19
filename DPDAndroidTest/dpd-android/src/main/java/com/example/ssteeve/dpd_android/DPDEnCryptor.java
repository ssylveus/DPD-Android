//https://gist.github.com/JosiasSena/3bf4ca59777f7dedcaf41a495d96d984
package com.example.ssteeve.dpd_android;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

class DPDEnCryptor {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private static String IV_IDENTIFIER = "_DPD_IV";

    DPDEnCryptor() {
    }

    String encryptText(final String alias, final String textToEncrypt)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        String base64IV = Base64.encodeToString(cipher.getIV(), Base64.DEFAULT);
        DPDHelper.saveToSharedPreference(base64IV, alias + IV_IDENTIFIER);

        try {
            return Base64.encodeToString(cipher.doFinal(textToEncrypt.getBytes("UTF-8")), Base64.DEFAULT);
        } catch (Exception e) {
            return  null;
        }
    }

    @NonNull
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build());
        }

        return keyGenerator.generateKey();
    }

    byte[] getIv(String alias) {
        return Base64.decode(DPDHelper.getSavedObj(alias + IV_IDENTIFIER), Base64.DEFAULT);
    }
}
