package com.example.mpos.data.deals.json


import com.google.gson.annotations.SerializedName

data class AddOnMenu(
    @SerializedName("description") val description: String,
    @SerializedName("itemlist") val itemList: List<ItemList>,
    @SerializedName("MenuCode") val menuCode: String,
    @SerializedName("Price") val price: Double,
    @SerializedName("Type") val type: String
)