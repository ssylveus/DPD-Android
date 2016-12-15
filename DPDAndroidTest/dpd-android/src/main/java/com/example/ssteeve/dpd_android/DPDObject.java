package com.example.ssteeve.dpd_android;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Response;

/**
 * Created by ssteeve on 11/2/16.
 */
public class DPDObject {

    @JsonProperty("id")
    public String objectId = null;
    public Integer createdAt = null;
    public Integer updatedAt = null;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String toJsonString() {
        String jsonString = null;
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static List<DPDObject> convertToMNObject(String jsonString, final Class<DPDObject> mappableObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, mappableObject);
            return mapper.readValue(jsonString, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createObject(String endPoint, @Nullable final Class mappableObject, final
    MappableResponseCallBack callBack) {
        String jsonString = toJsonString();
        DPDRequest.makeRequest(endPoint, null, HTTPMethod.POST, jsonString, null, mappableObject, new
                RequestCallBack() {
                    @Override
                    public void onResponse(String jsonString) {
                        String arrayValue = "";
                        if (jsonString.startsWith("{")) {
                            arrayValue = "[" + jsonString + "]"; //Converting jsonObject to JsonArray
                        } else {
                            arrayValue = jsonString;
                        }
                        callBack.onResponse(DPDObject.convertToMNObject(arrayValue, mappableObject));
                    }

                    @Override
                    public void onFailure(Response response) {
                        callBack.onFailure(response);
                    }
                });
    }

    public void updateObject(String endPoint, final Class mappableObject, final
    MappableResponseCallBack callBack) {
        String jsonString = toJsonString();
        DPDRequest.makeRequest(endPoint, null, HTTPMethod.POST, jsonString, null, mappableObject, new
                RequestCallBack() {
                    @Override
                    public void onResponse(String jsonString) {
                        String arrayValue = "";
                        if (jsonString.startsWith("{")) {
                            arrayValue = "[" + jsonString + "]"; //Converting jsonObject to JsonArray
                        } else {
                            arrayValue = jsonString;
                        }
                        callBack.onResponse(DPDObject.convertToMNObject(arrayValue, mappableObject));
                    }

                    @Override
                    public void onFailure(Response response) {
                        callBack.onFailure(response);
                    }
                });
    }

    public void deleteObject(String endPoint, @Nullable HashMap<String, Object> queryParam, final
    RequestCallBack callBack) {
        DPDRequest.makeRequest(endPoint + "/" + this.objectId, queryParam, HTTPMethod.DELETE, null, null,
                null, callBack);
    }
}
