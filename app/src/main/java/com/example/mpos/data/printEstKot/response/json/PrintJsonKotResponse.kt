package com.example.mpos.data.printEstKot.response.json


import com.google.gson.annotations.SerializedName

data class PrintJsonKotResponse(
    @SerializedName("BaseAmount")
    val baseAmount: String,
    @SerializedName("BillType")
    val billType: String,
    @SerializedName("BillTypeTxt")
    val billTypeTxt: String,
    @SerializedName("childitemList")
    val childitemList: List<Any>,
    @SerializedName("HeaderTxt")
    val headerTxt: String,
    @SerializedName("HeaderTxt1")
    val headerTxt1: String,
    @SerializedName("HeaderTxt2")
    val headerTxt2: String,
    @SerializedName("HeaderTxt3")
    val headerTxt3: String,
    @SerializedName("HeaderTxt4")
    val headerTxt4: String,
    @SerializedName("HeaderTxt5")
    val headerTxt5: String,
    @SerializedName("HeaderTxt6")
    val headerTxt6: String,
    @SerializedName("HeaderTxt7")
    val headerTxt7: String,
    @SerializedName("SubHeaderTxt1")
    val subHeaderTxt1: String,
    @SerializedName("SubHeaderTxt2")
    val subHeaderTxt2: String,
    @SerializedName("SubHeaderTxt3")
    val subHeaderTxt3: String,
    @SerializedName("SubHeaderTxt4")
    val subHeaderTxt4: String,
    @SerializedName("SubHeaderTxt5")
    val subHeaderTxt5: String
)