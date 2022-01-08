package com.example.offiqlresturantapp.model.test.apkJanLogin.jsonfol


import com.google.gson.annotations.SerializedName

data class ResultApiLoginCls(
    @SerializedName("Message") val message: String,
    @SerializedName("Status") val status: Boolean,
    @SerializedName("StoreName") val storeName: String
)