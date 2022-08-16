package com.example.mpos.data.deals.json


import com.google.gson.annotations.SerializedName

data class ItemList(
    @SerializedName("description") val description: String,
    @SerializedName("itemcode") val itemCode: String,
    @SerializedName("Qty") val qty: String
)