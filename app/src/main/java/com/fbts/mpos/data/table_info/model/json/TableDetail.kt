package com.fbts.mpos.data.table_info.model.json


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Table_for_status")
data class TableDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("GuestNumber") val guestNumber: String,
    @SerializedName("Status") val status: String,
    @SerializedName("Table No.") val tableNo: String,
    @SerializedName("MPOSDoc") val receiptNo: String
) : Parcelable

enum class TblStatus {
    Free,
    Occupied,
    Reserved,
}