package com.example.mpos.delete.pine.response;

import com.google.gson.annotations.SerializedName;

public class Header{
    @SerializedName("ApplicationId") 
    public String applicationId;
    @SerializedName("MethodId") 
    public String methodId;
    @SerializedName("UserId") 
    public String userId;
    @SerializedName("VersionNo") 
    public String versionNo;
}

