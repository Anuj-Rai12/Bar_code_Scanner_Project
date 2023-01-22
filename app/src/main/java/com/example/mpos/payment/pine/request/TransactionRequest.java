package com.example.mpos.payment.pine.request;


import com.google.gson.annotations.SerializedName;
import com.example.mpos.payment.unit.GsonUtils;

public class TransactionRequest {
    @SerializedName("APP_ID")
    public String aPP_ID;
    @SerializedName("Detail")
    public Detail detail;
    @SerializedName("Header")
    public Header header;

    @Override
    public String toString() {
        return GsonUtils.fromJsonToString(this);
    }
}
