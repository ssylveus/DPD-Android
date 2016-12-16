package com.example.ssteeve.dpd_android;

import android.support.annotation.Nullable;
import android.util.Log;


import com.fasterxml.jackson.annotation.JsonIgnore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/20/16.
 */
public class DPDUser extends DPDObject {
    private static DPDUser ourInstance = new DPDUser();
    private static String sAccessTokenEndpoint = "accesstoken";

    public static DPDUser getInstance() {
        return ourInstance;
    }

    private String username;
    private String password;


    protected DPDUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static DPDUser currentUser(Class mapper) {
        String jsonString = DPDHelper.getObjFromSharedPreference(DPDConstants.SHARED_PREFS_USER_KEY);
         return (DPDUser) DPDObject.convertToMNObject(jsonString, mapper).get(0);
    }

    static void saveUserObjectToSharedPreference(String jsonString) {
        DPDHelper.saveObjToSharedPreference(jsonString, DPDConstants.SHARED_PREFS_USER_KEY);
    }

    static public void createUser(final String endPoint, final String username,
                                  final String password, final Class mappableObject,
                                  final MappableResponseCallBack callBack) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        DPDRequest.makeRequest(endPoint, null, HTTPMethod.POST, jsonObject.toString(), null, null, new RequestCallBack() {

            @Override
            public void onResponse(String jsonString) {
                try {
                    login(endPoint + "/login", username, password, mappableObject, callBack);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }
        });
    }

    static public void login(final String endPoint, String username,
                             final String password, final Class mappableObject,
                             final MappableResponseCallBack callBack) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        DPDRequest.makeRequest(endPoint, null, HTTPMethod.POST, jsonObject.toString(), null, null, new RequestCallBack() {

            @Override
            public void onResponse(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.has("id")) {
                        String sessionToken = jsonObject.getString("id");
                        DPDCredentials.getInstance().setSessionId(sessionToken);
                        retrieveAccessToken(sAccessTokenEndpoint, mappableObject, callBack);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }
        });

    }

    static private void retrieveAccessToken(String endPoint,
                                     final Class mappableObject,
                                     final MappableResponseCallBack callBack) {

        DPDRequest.makeRequest(endPoint, null, HTTPMethod.GET, null, null, mappableObject, new RequestCallBack() {


            @Override
            public void onResponse(String jsonString) throws JSONException {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(jsonString);

                if (jsonObject.has("accessToken")) {
                    DPDCredentials.getInstance().setAccessToken(jsonObject.getString("accessToken"));
                }

                if (jsonObject.has("sessionId")) {
                    DPDCredentials.getInstance().setSessionToken(jsonObject.getString("sessionId"));
                }

                if (jsonObject != null && jsonObject.has("user")) {
                    Object userObj = jsonObject.get("user");
                    String arrayValue = "";
                    if (userObj.toString().startsWith("{")) {
                        arrayValue = "[" + userObj.toString() + "]"; //Converting jsonObject to JsonArray
                    } else {
                        arrayValue = userObj.toString().toString();
                    }
                    saveUserObjectToSharedPreference(arrayValue);
                    callBack.onResponse(DPDObject.convertToMNObject(arrayValue, mappableObject));
                }

            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }

        });

    }
}


