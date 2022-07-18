package com.example.mpos.data.mnu.response.json


import com.google.gson.annotations.SerializedName

data class SubMenu(
    @SerializedName("description") val description: String?,
    @SerializedName("itemlist") val itemList: List<ItemList>?,
    @SerializedName("message") val message: String?,
    @SerializedName("parameter") val parameter: String?,
    @SerializedName("Status") val status: String?,
    @SerializedName("SubCatkey") val subCatKey: String?
)