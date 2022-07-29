package com.example.mpos.data.login.model.api.json

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ApkLoginJsonResponse(
    @SerializedName("Message") val message: String,
    @SerializedName("Status") val status: Boolean,
    @SerializedName("StoreName") val storeName: String,
    @SerializedName("screenlist")val screenList: List<String>
) : Parcelable
