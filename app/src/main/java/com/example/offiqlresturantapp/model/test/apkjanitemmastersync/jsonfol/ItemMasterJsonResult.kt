package com.example.offiqlresturantapp.model.test.apkjanitemmastersync.jsonfol


import com.google.gson.annotations.SerializedName

data class ItemMasterJsonResult(
    @SerializedName("ItemMaster") val itemMaster: List<ItemMaster>
)