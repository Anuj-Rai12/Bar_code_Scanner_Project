package com.example.mpos.delete.pine.response;


import com.google.gson.annotations.SerializedName;
import com.example.mpos.delete.unit.GsonUtils;

public class TransactionResponse {
    @SerializedName("Header")
    public Header header;
    @SerializedName("Response")
    public Response response;

    @Override
    public String toString() {
        return GsonUtils.fromJsonToString(this);
    }
}
