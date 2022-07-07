package com.fbts.mpos.data.item_master_sync.json


import com.google.gson.annotations.SerializedName

data class ItemMethodSyncJsonResponse(
    @SerializedName("ItemMaster") val itemMaster: List<ItemMaster>
)