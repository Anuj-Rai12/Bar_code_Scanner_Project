package com.example.mpos.delete.pine.response;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.example.mpos.delete.pine.AppConfig;
import com.example.mpos.delete.unit.GsonUtils;



/*
 * Created by Pinelabs Pvt Ltd on 4/5/2018.
 */

public class BaseResponse {

    @SerializedName("OperationType")
    private int operationType;

    @SerializedName("ResponseCode")
    private int responseCode;

    @SerializedName("ResponseMessage")
    private String responseMessage;

    public Bundle getBundle() {

        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.RESPONSE_KEY, new Gson().toJson(this));

        return bundle;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return GsonUtils.fromJsonToString(this);
    }
}