package com.example.mpos.data.confirmOrder.response.json


import com.google.gson.annotations.SerializedName

data class ItemList(
    @SerializedName("Amount") val amount: String,
    @SerializedName("description") val description: String,
    @SerializedName("itemcode") val itemCode: String,
    @SerializedName("price") val price: String,
    @SerializedName("Qty") val qty: Double
)