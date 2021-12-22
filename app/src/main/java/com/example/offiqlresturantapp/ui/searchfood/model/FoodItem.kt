package com.example.offiqlresturantapp.ui.searchfood.model

//Sample Class to Provide sample data
data class FoodItem(
    val foodName: String,
    val foodPrice: Int,
    val foodOffer: String?,
    var foodQTY: Int = 0,
    var offerDesc: List<String>?,
    var addWithOffer: Boolean = true
)