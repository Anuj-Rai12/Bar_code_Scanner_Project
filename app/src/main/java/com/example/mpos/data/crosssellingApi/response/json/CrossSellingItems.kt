package com.example.mpos.data.crosssellingApi.response.json


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class CrossSellingItems(
    @SerializedName("childTxt") val childTxt: String,
    @SerializedName("itemcode") val itemCode: String,
    @SerializedName("price") val price: String
) : Parcelable