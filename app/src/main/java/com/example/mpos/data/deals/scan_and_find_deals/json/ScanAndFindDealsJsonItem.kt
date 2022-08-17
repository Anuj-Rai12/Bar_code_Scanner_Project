package com.example.mpos.data.deals.scan_and_find_deals.json


import com.google.gson.annotations.SerializedName

data class ScanAndFindDealsJsonItem(
    @SerializedName("Barcode") val barcode: String,
    @SerializedName("ItemCategory") val itemCategory: String,
    @SerializedName("ItemCode") val itemCode: String,
    @SerializedName("ItemDescription") val itemDescription: String,
    @SerializedName("ItemName") val itemName: String,
    @SerializedName("Qty") val qty: Int,
    @SerializedName("SalePrice") val salePrice: String,
    @SerializedName("UOM") val uOM: String
)