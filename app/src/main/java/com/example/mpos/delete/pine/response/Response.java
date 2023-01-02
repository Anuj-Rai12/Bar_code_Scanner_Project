package com.example.mpos.delete.pine.response;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("AppVersion")
    public String appVersion;
    @SerializedName("ParameterJson")
    public String parameterJson;
    @SerializedName("ResponseCode")
    public int responseCode;
    @SerializedName("ResponseMsg")
    public String responseMsg;
}
