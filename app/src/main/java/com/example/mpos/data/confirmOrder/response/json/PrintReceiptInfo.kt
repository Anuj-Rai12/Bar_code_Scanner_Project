package com.example.mpos.data.confirmOrder.response.json


import com.google.gson.annotations.SerializedName

data class PrintReceiptInfo(
    @SerializedName("AmtExclGST") val amtExclGST: String,
    @SerializedName("AmtInclGST") val amtInclGST: String,
    @SerializedName("Datetime") val datetime: String,
    @SerializedName("HeaderTxt") val headerTxt: String,
    @SerializedName("HeaderTxt2") val headerTxt2: String,
    @SerializedName("itemcount") val itemCount: String,
    @SerializedName("itemlist") val itemList: List<ItemList>,
    @SerializedName("orderid") val orderId: String
)