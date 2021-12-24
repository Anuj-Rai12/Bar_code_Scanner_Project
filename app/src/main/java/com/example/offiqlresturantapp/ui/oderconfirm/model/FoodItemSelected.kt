package com.example.offiqlresturantapp.ui.oderconfirm.model

import com.example.offiqlresturantapp.ui.searchfood.model.OfferDesc

data class FoodItemSelected(
    val foodName: String,
    val foodQty: Int,
    val foodRate: Int,
    val foodAmt: Int,
    val offerDesc: List<OfferDesc>? = null,
    val bg: Int
)
