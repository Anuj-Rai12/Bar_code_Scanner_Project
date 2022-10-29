package com.example.mpos.data.crosssellingApi.response.json


import com.google.gson.annotations.SerializedName

data class CrossSellingItems(
    @SerializedName("childTxt") val childTxt: String,
    @SerializedName("itemcode") val itemCode: String,
    @SerializedName("price") val price: String
)