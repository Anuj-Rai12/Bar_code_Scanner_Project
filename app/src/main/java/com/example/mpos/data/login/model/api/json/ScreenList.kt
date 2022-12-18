package com.example.mpos.data.login.model.api.json


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenList(
    @SerializedName("DynamicMenuEnable") val dynamicMenuEnable: Boolean,
    @SerializedName("screencaption") val screenCaption: String,
    @SerializedName("screenlist") val screenList: String
) : Parcelable