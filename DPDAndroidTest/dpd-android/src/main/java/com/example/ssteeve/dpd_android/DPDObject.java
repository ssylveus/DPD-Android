package com.example.ssteeve.dpd_android;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ssteeve on 11/2/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DPDObject {

    private String objectId = null;
    private Long createdAt = null;
    private Long updatedAt = null;

    public String getObjectId() {
        return objectId;
    }

    @JsonSetter("id")
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
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

    public static List<DPDObject> convertToMNObject(String jsonString, final Class<DPDObject> mappableObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, mappableObject);
            return mapper.readValue(jsonString, type);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

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

    public void updateObject(String endPoint, final Class mappableObject, final
    MappableResponseCallBack callBack) {
        String jsonString = toJsonString();
        DPDRequest.makeRequest(endPoint + "/" + objectId, null, HTTPMethod.PUT, jsonString, null, mappableObject, new
                RequestCallBack() {
                    @Override
                    public void onResponse(String jsonString) {
                        String arrayValue = "";
                        if (jsonString.startsWith("{")) {
                            arrayValue = "[" + jsonString + "]"; //Converting jsonObject to JsonArray
                        } else {
                            arrayValue = jsonString;
                        }
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

    public void deleteObject(String endPoint, @Nullable HashMap<String, Object> queryParam, final
    RequestCallBack callBack) {
        DPDRequest.makeRequest(endPoint + "/" + this.objectId, queryParam, HTTPMethod.DELETE, null, null,
                null, callBack);
    }
}
