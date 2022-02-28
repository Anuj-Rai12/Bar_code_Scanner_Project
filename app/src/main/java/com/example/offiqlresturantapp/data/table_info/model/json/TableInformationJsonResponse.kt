package com.example.offiqlresturantapp.data.table_info.model.json


import com.google.gson.annotations.SerializedName

data class TableInformationJsonResponse(
    @SerializedName("TableDetails") val tableDetails: List<TableDetail>,
    @SerializedName("Total Table count") val totalTableCount: Int
)