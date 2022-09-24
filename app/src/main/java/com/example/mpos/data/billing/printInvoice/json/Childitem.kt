package com.example.mpos.data.billing.printInvoice.json


import com.google.gson.annotations.SerializedName

data class Childitem(
    @SerializedName("Amount") val amount: String,
    @SerializedName("description") val description: String,
    @SerializedName("itemcode") val itemcode: String,
    @SerializedName("price") val price: String,
    @SerializedName("Qty") val qty: String
)