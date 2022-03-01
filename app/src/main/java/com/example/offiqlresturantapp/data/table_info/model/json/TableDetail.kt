package com.example.offiqlresturantapp.data.table_info.model.json


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TableDetail(
    @SerializedName("GuestNumber") val guestNumber: String,
    @SerializedName("Status") val status: String,
    @SerializedName("Table No.") val tableNo: String
) : Parcelable

enum class TblStatus {
    Free,
    Occupied,
    Reserved,
}