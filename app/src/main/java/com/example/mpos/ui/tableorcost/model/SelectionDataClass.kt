package com.example.mpos.ui.tableorcost.model

import com.example.mpos.R

//Sample Model Class
data class SelectionDataClass(
    val image: Int,
    val title: String,
    val type: String
) {
    companion object {
        val tblManagement = SelectionDataClass(
            image = R.drawable.ic_waiter,
            title = "Table Management",
            type = RestaurantSelection.TABLEMGT.name
        )
        val tblReservation = SelectionDataClass(
            image = R.drawable.ic_table_dinner,
            title = "Table Reservation",
            type = RestaurantSelection.TABLERESERVATION.name
        )
        val cost = SelectionDataClass(
            image = R.drawable.ic_cost_estimation,
            title = "Cost Estimate",
            type = RestaurantSelection.ESTIMATION.name
        )
        val bill = SelectionDataClass(
            image = R.drawable.ic_receipt_bill,
            title = "Bill Payment",
            type = RestaurantSelection.BILLING.name
        )
        val showRoom = SelectionDataClass(
            image = R.drawable.showroom_estimation,
            title = "Showroom Estimation",
            type = RestaurantSelection.SHOWROOMESTIMATE.name
        )
        val restaurant = SelectionDataClass(
            image = R.drawable.restaurant_estimation,
            title = "Restaurant Estimation",
            type = RestaurantSelection.RESTAURANTESTIMATE.name
        )

        enum class RestaurantSelection {
            TABLEMGT,
            TABLERESERVATION,
            ESTIMATION,
            BILLING,
            SHOWROOMESTIMATE,
            RESTAURANTESTIMATE,
        }

    }

}