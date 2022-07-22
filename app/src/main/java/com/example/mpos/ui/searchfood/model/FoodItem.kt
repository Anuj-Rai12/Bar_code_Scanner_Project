package com.example.mpos.ui.searchfood.model

import android.os.Parcelable
import com.example.mpos.data.item_master_sync.json.ItemMaster
import com.example.mpos.utils.listOfBg
import kotlinx.parcelize.Parcelize

//Sample Class to Provide sample data
@Parcelize
data class FoodItem(
    val foodName: String,
    val foodPrice: Int,
    val foodOffer: String?,
    var foodQTY: Int = 1,
    var foodAmt: Int,
    var offerDesc: List<OfferDesc>?,
    val bg: Int = listOfBg[0],
    //var addWithOffer: Boolean = true
) : Parcelable

@Parcelize
data class ItemMasterFoodItem(
    val itemMaster: ItemMaster,
    var foodQty: Int,
    var foodAmt: Int,
    val bg: Int = listOfBg.first(),
    val free_txt: String = ""
) : Parcelable


@Parcelize
data class FoodItemList(
    val foodList: List<ItemMasterFoodItem>
) : Parcelable


@Parcelize
data class OfferDesc(
    val price: Int,
    val title: String,
    var selected: Boolean = true
) : Parcelable