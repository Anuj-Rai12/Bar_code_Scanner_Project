package com.example.offiqlresturantapp.data.occupied.json


import com.google.gson.annotations.SerializedName

data class OccupiedTableItem(
    @SerializedName("foodAmt") val foodAmt: Double,
    @SerializedName("foodQty") val foodQty: Int,
    @SerializedName("itemMaster") val itemMaster: ItemMaster
)