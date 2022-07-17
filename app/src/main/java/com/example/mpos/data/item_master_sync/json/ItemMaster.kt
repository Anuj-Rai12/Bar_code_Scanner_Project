package com.example.mpos.data.item_master_sync.json


import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "ItemMaster_tbl")
data class ItemMaster(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @SerializedName("Barcode") val barcode: String,
    @SerializedName("ItemCategory") val itemCategory: String,
    @SerializedName("ItemCode") val itemCode: String,
    @SerializedName("ItemDescription") val itemDescription: String,
    @SerializedName("ItemName") val itemName: String,
    @SerializedName("SalePrice") val salePrice: String,
    @SerializedName("UOM") val uOM: String,

    ) : Parcelable {
    @IgnoredOnParcel
    var foodQty: Int = 1

    @IgnoredOnParcel
    var foodAmt = try {
        salePrice.toInt()
    } catch (e: Exception) {
        Log.i("ItemMaster", "error: ${e.localizedMessage}")
        0
    }
}