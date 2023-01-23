package com.example.mpos.payment.pine.response;


import com.google.gson.annotations.SerializedName;
import com.example.mpos.payment.unit.GsonUtils;

public class TransactionResponse {
    @SerializedName("Header")
    public Header header;
    @SerializedName("Response")
    public Response response;
    @SerializedName("Detail")
    public DetailTrans detail;

    @Override
    public String toString() {
        return GsonUtils.fromJsonToString(this);
    }
}
