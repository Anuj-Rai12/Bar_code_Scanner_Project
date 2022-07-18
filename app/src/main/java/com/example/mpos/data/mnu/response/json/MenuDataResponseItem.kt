package com.example.mpos.data.mnu.response.json


import com.google.gson.annotations.SerializedName

data class MenuDataResponseItem(
    @SerializedName("Catkey") val catKey: String,
    @SerializedName("Description") val description: String,
    @SerializedName("SubMenu") val subMenu: List<SubMenu>
)