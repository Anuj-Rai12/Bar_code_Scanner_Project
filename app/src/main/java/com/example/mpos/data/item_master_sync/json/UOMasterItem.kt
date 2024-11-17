package com.example.mpos.data.item_master_sync.json

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UOMasterItem(
    @SerializedName("ItemUOM")
    val itemUom: String,
    @SerializedName("ItemSalePrice")
    val itemSalePrice: String,
    @SerializedName("UOMQty")
    val uomqty: String,
) : Parcelable
