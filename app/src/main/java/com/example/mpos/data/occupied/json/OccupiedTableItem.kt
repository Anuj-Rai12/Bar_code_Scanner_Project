package com.example.mpos.data.occupied.json


import com.google.gson.annotations.SerializedName

data class OccupiedTableItem(
    @SerializedName("foodAmt") val foodAmt: Double,
    @SerializedName("foodQty") val foodQty: Double,
    @SerializedName("itemMaster") val itemMaster: ItemMaster
)