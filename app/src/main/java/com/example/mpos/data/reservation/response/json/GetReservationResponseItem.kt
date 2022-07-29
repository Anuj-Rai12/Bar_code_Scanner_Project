package com.example.mpos.data.reservation.response.json


import com.google.gson.annotations.SerializedName

data class GetReservationResponseItem(
    @SerializedName("cover") val cover: String,
    @SerializedName("custmobile") val customerMobile: String,
    @SerializedName("custname") val customerName: String,
    @SerializedName("instruction") val instruction: String,
    @SerializedName("ReserveTime") val reserveTime: String,
    @SerializedName("Reservedate") val reserveDate: String
)