package com.fbts.mpos.data.occupied.json


import com.google.gson.annotations.SerializedName

data class ItemMaster(
    @SerializedName("Barcode") val barcode: String,
    @SerializedName("foodAmt") val foodAmt: Double,
    @SerializedName("foodQty") val foodQty: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("ItemCategory") val itemCategory: String,
    @SerializedName("ItemCode") val itemCode: String,
    @SerializedName("ItemDescription") val itemDescription: String,
    @SerializedName("ItemName") val itemName: String,
    @SerializedName("SalePrice") val salePrice: String,
    @SerializedName("UOM") val uOM: String
)