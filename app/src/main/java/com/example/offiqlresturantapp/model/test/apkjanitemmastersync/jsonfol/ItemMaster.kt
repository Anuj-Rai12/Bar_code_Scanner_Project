package com.example.offiqlresturantapp.model.test.apkjanitemmastersync.jsonfol


import com.google.gson.annotations.SerializedName

data class ItemMaster(
    @SerializedName("Barcode") val barcode: String,
    @SerializedName("ItemCode") val itemCode: String,
    @SerializedName("ItemDescription") val itemDescription: String,
    @SerializedName("RSGST") val rSGST: String,
    @SerializedName("SRGST") val sRGST: String,
    @SerializedName("SalePrice") val salePrice: String,
    @SerializedName("UOM") val uOM: String
)