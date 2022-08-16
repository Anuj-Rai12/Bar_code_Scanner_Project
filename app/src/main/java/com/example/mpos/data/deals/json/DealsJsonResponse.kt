package com.example.mpos.data.deals.json


import com.google.gson.annotations.SerializedName

data class DealsJsonResponse(
    @SerializedName("AddOnMenu") val addOnMenu: List<AddOnMenu>
)