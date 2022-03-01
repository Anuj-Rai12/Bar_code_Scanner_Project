package com.example.offiqlresturantapp.data.item_master_sync.json


import com.google.gson.annotations.SerializedName

data class ItemMaster(
    @SerializedName("Barcode") val barcode: String,
    @SerializedName("ItemCategory") val itemCategory: String,
    @SerializedName("ItemCode") val itemCode: String,
    @SerializedName("ItemDescription") val itemDescription: String,
    @SerializedName("ItemName") val itemName: String,
    @SerializedName("SalePrice") val salePrice: String,
    @SerializedName("UOM") val uOM: String,

    ) {
    var foodQty: Int = 1
    var foodAmt = salePrice.toInt()
}