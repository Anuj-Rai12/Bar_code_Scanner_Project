package com.example.mpos.data.reservation.response.json.addRequest


import com.google.gson.annotations.SerializedName

data class AddReservationJsonResponse(
    @SerializedName("Message") val message: String,
    @SerializedName("Status") val status: Boolean
)