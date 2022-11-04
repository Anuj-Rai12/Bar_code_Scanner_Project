package com.example.mpos.ui.searchfood.model

import android.os.Parcelable
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingItems
import com.example.mpos.data.crosssellingApi.response.json.CrossSellingJsonResponse
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
    var foodQty: Double,
    var foodAmt: Double,
    val bg: Int = listOfBg.first(),
    val free_txt: String = "",
    val isDeal: Boolean = false,
    val crossSellingItems: CrossSellingJsonResponse?=null
) : Parcelable


@Parcelize
data class FoodItemList(
    val foodList: List<ItemMasterFoodItem>
) : Parcelable


@Parcelize
data class OfferDesc(
    val price: Double,
    val title: String,
    var selected: Boolean = true
) : Parcelable