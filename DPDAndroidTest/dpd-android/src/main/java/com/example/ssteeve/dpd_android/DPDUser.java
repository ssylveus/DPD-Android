package com.example.ssteeve.dpd_android;

import android.support.annotation.Nullable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/20/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DPDUser extends DPDObject {
    private static DPDUser ourInstance = new DPDUser();

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

    public static DPDUser currentUser(Class mapper) throws IOException {
        String jsonString = DPDHelper.getSavedObj(DPDConstants.SHARED_PREFS_USER_KEY);
        if (jsonString != null) {
            return (DPDUser) DPDObject.convertToMNObject(jsonString, mapper).get(0);
        }

        return null;
    }

    static void saveUserObjectToSharedPreference(String jsonString) {
        DPDHelper.saveObject(jsonString, DPDConstants.SHARED_PREFS_USER_KEY);
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
                    login(endPoint, username, password, mappableObject, callBack);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFailure(null, null, e);
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

        DPDRequest.makeRequest(endPoint + "/" + "login", null, HTTPMethod.POST, jsonObject.toString(), null, null, new RequestCallBack() {

            @Override
            public void onResponse(String jsonString) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    if (jsonObject.has("id")) {
                        String sessionToken = jsonObject.getString("id");
                        DPDCredentials.getInstance().setSessionId(sessionToken);

                        if (DPDConstants.sSupportAccessToken && DPDConstants.sAccessTokenEndPoint != null) {
                            retrieveAccessToken(DPDConstants.sAccessTokenEndPoint, mappableObject, callBack);
                        } else {
                            String userId = jsonObject.getString("uid");
                            getUser(endPoint, userId, mappableObject, callBack);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFailure(null, null, e);
                }
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }
        });

    }

    static public void getUser(final String endPoint, final String userId, final Class mappableObject, final MappableResponseCallBack callBack) throws JSONException {
        DPDRequest.makeRequest(endPoint + "/" + userId, null, HTTPMethod.GET, null, null, null, new RequestCallBack() {

            @Override
            public void onResponse(String jsonString) {
                String arrayValue = "[" + jsonString + "]"; //Converting jsonObject to JsonArray
                saveUserObjectToSharedPreference(arrayValue);

                try {
                    callBack.onResponse(DPDObject.convertToMNObject(arrayValue, mappableObject));
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onFailure(null, null, e);
                }
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }
        });
    }

    public void updateUser(String endPoint, Class mappableObject, MappableResponseCallBack responseCallBack) {
        updateObject(endPoint, mappableObject, responseCallBack);
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
                    try {
                        callBack.onResponse(DPDObject.convertToMNObject(arrayValue, mappableObject));
                    } catch (IOException e) {
                        e.printStackTrace();
                        callBack.onFailure(null, null, e);
                    }
                }

            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }

        });

    }

    static public void logout(String endpoint, final ResponseCallBack callBack) {
        if (DPDCredentials.getInstance().getSessionId() != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Cookie", DPDCredentials.getInstance().getSessionId());
                DPDRequest.makeRequest(endpoint + "/" + "logout", null, HTTPMethod.POST, jsonObject.toString(), null, null, new RequestCallBack() {

                    @Override
                    public void onResponse(String jsonString) throws Exception {
                        callBack.onResponse("Successful logout");
                    }

                    @Override
                    public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                        callBack.onFailure(null, null, new Exception("Failed to logout"));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            callBack.onFailure(null, null, new Exception("No active session. Could not logout."));
        }

        DPDCredentials.clear();
    }
}


