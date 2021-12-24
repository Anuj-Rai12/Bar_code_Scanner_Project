package com.example.offiqlresturantapp.ui.searchfood.model

import android.os.Parcelable
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
    //var addWithOffer: Boolean = true
) : Parcelable

@Parcelize
data class FoodItemList(
    val foodList: List<FoodItem>
) : Parcelable


@Parcelize
data class OfferDesc(
    val price: Int,
    val title: String,
    var selected: Boolean = true
) : Parcelable