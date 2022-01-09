package com.example.offiqlresturantapp.ui.testingconnection.model.api.json

import com.google.gson.annotations.SerializedName

data class ApkLoginJsonResponse(
    @SerializedName("Message") val message: String,
    @SerializedName("Status") val status: Boolean,
    @SerializedName("StoreName") val storeName: String
)
