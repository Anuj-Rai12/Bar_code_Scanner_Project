package com.example.mpos.data.crosssellingApi.response.json


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CrossSellingJsonResponse(
    @SerializedName("childitemList") val childItemList: List<ChilditemList>,
    @SerializedName("Description") val description: String,
    @SerializedName("MaxSelection") val maxSelection: String,
    @SerializedName("MinSelection") val minSelection: String,
    @SerializedName("parentitem") val parentItem: String
) : Parcelable


@Parcelize
data class ChilditemList(
    @SerializedName("ItemCode")
    val itemCode: String,
    @SerializedName("ItemDesc")
    val itemDesc: String,
    @SerializedName("MinSelection")
    val minSelection: String,
    @SerializedName("MaxSelection")
    val maxSelection: String,
    @SerializedName("skipqtylinking")
    val skipqtylinking: String,
    @SerializedName("ChildList")
    val childList: List<CrossSellingItems>,
) : Parcelable