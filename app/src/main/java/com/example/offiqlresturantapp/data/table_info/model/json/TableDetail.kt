package com.example.offiqlresturantapp.data.table_info.model.json


import com.google.gson.annotations.SerializedName

data class TableDetail(
    @SerializedName("GuestNumber") val guestNumber: String,
    @SerializedName("Status") val status: String,
    @SerializedName("Table No.") val tableNo: String
)

enum class TblStatus {
    Free,
    Occupied,
    Reserved,
}