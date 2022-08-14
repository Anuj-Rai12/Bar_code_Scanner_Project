package com.example.mpos.data.confirmOrder.response.json


import com.google.gson.annotations.SerializedName

data class ItemList(
    @SerializedName("Amount") val amount: Int,
    @SerializedName("description") val description: String,
    @SerializedName("itemcode") val itemCode: String,
    @SerializedName("price") val price: Int,
    @SerializedName("Qty") val qty: Int
)