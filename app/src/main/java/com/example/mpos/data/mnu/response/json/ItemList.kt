package com.example.mpos.data.mnu.response.json


import com.google.gson.annotations.SerializedName

data class ItemList(
    @SerializedName("description") val description: String,
    @SerializedName("itemcode") val itemCode: String,
    @SerializedName("sort") val sort: String
)