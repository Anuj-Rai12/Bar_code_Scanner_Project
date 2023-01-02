package com.example.mpos.delete.pine.request;


import com.google.gson.annotations.SerializedName;
import com.example.mpos.delete.unit.GsonUtils;

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
