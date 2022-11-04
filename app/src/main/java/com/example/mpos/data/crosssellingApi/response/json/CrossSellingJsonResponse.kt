package com.example.mpos.data.crosssellingApi.response.json


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellingJsonResponse(
    @SerializedName("childitemList") val childItemList: List<CrossSellingItems>,
    @SerializedName("Description") val description: String,
    @SerializedName("MaxSelection") val maxSelection: String,
    @SerializedName("MinSelection") val minSelection: String,
    @SerializedName("parentitem") val parentItem: String
) : Parcelable