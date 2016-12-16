package com.example.ssteeve.dpd_android;

import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by ssteeve on 11/2/16.
 */
public class DPDQuery {

    private Integer mQueryCondition = QueryCondition.NONE;
    private Integer mSortingOrder = OrderType.ASCENDING;
    private Integer mLimit = null;
    private Integer mSkip = null;
    private String mQueryField = null;
    private Object mQueryFieldValue = null;
    private String mSortField = null;
    private Map<String, Object> mQueryInfo = new HashMap<>();

    public DPDQuery(Integer queryCondition, Integer sortingOrder, Integer limit, Integer skip, String queryField,
                    Object queryFieldValue, String sortField) {
        mQueryCondition = queryCondition;
        mSortingOrder = sortingOrder;
        mLimit = limit;
        this.mSkip = skip;
        this.mQueryField = queryField;
        this.mQueryFieldValue = queryFieldValue;
        this.mSortField = sortField;
    }

    public void findObject(String endPoint, final ResponseCallBack callBack) {
        processQueryInfo();
        DPDRequest.makeRequest(endPoint, null, HTTPMethod.GET, null, null, null, new RequestCallBack() {

            @Override
            public void onResponse(String jsonString) {
                callBack.onResponse(jsonString);
            }

            @Override
            public void onFailure(@Nullable Call call, @Nullable Response response, @Nullable Exception e) {
                callBack.onFailure(call, response, e);
            }
        });
    }

    public void findMappableObject(String endPoint, @Nullable final Class mappableObject, final MappableResponseCallBack callBack) {
        processQueryInfo();
        DPDRequest.makeRequest(endPoint, mQueryInfo, HTTPMethod.GET, null, null, mappableObject, new RequestCallBack() {

            @Override
            public void onResponse(String jsonString) {
                String arrayValue;
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


    private void processQueryInfo() {
        if (mQueryFieldValue != null && mQueryCondition != null) {
            processQueryCondition();
        }

        if (mSortField != null) {
            mQueryInfo.putAll(processSortingOrder());
        }

        if (mLimit != null) {
            mQueryInfo.putAll(processLimit());
        }

        if (mSkip != null) {
            mQueryInfo.putAll(processSkip());
        }
    }

    private void processQueryCondition() {
        if (mQueryCondition.equals(QueryCondition.GREATER_THAN)) {
            mQueryInfo.putAll(processGreatherThan());
        } else if (mQueryCondition.equals(QueryCondition.GREATER_THAN_EQUAL_TO)) {
            mQueryInfo.putAll(processGreatherThanEqualTo());
        } else if (mQueryCondition.equals(QueryCondition.LESS_THAN)) {
            mQueryInfo.putAll(processLessThan());
        } else if (mQueryCondition.equals(QueryCondition.LESS_THAN_EQUAL_TO)) {
            mQueryInfo.putAll(processLessThanEqualTo());
        } else if (mQueryCondition.equals(QueryCondition.EQUAL)) {
            mQueryInfo.putAll(processEqualTo());
        } else if (mQueryCondition.equals(QueryCondition.NOT_EQUAL)) {
            mQueryInfo.putAll(processNotEqualTo());
        } else if (mQueryCondition.equals(QueryCondition.CONTAINS)) {
            mQueryInfo.putAll(processContain());
        } else if (mQueryCondition.equals(QueryCondition.REGEX)) {
            mQueryInfo.putAll(processRegex());
        } else if (mQueryCondition.equals(QueryCondition.OR)) {
            mQueryInfo.putAll(processOr());
        } else if (mQueryCondition.equals(QueryCondition.AND)) {
            mQueryInfo.putAll(processAnd());
        } else {
            mQueryInfo.putAll(processEqualTo());
        }
    }

    private Map<String, Object> processGreatherThan() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$gt", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processGreatherThanEqualTo() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$gte", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processLessThan() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$lt", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processLessThanEqualTo() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$lte", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processNotEqualTo() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$ne", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processEqualTo() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$eq", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processContain() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$in", mQueryFieldValue);
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processRegex() {
        Map<String, Object> queryParam = new HashMap<>();
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$regex", mQueryFieldValue);
        valueObj.put("$options", "i");
        queryParam.put(mQueryField, valueObj);
        return queryParam;
    }

    private Map<String, Object> processSortingOrder() {
        Map<String, Object> sortingParam = new HashMap<>();
        Map<String, Object> sortingObj = new HashMap<>();
        sortingObj.put(mSortField, mSortingOrder);
        sortingParam.put("$sort", sortingObj);
        return sortingParam;
    }


    private Map<String, Object> processOr() {
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$or", mQueryFieldValue);
        return valueObj;
    }

    private Map<String, Object> processAnd() {
        Map<String, Object> valueObj = new HashMap<>();
        valueObj.put("$and", mQueryFieldValue);
        return valueObj;
    }

    private Map<String, Object> processLimit() {
        Map<String, Object> limitParam = new HashMap<>();
        limitParam.put("$limit", mLimit);
        return limitParam;
    }

    private Map<String, Object> processSkip() {
        Map<String, Object> skipParam = new HashMap<>();
        skipParam.put("$skip", mSkip);
        return skipParam;
    }
}







