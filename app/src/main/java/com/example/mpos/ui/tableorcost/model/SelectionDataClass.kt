package com.example.mpos.ui.tableorcost.model

import com.example.mpos.R

//Sample Model Class
data class SelectionDataClass(
    val image: Int,
    val title: String,
    val type: String
) {
    companion object {
        val list = listOf(
            SelectionDataClass(
                image = R.drawable.ic_waiter,
                title = "Table Management",
                type = RestaurantSelection.TABLE_MANAGEMENT.name
            ),
            SelectionDataClass(
                image = R.drawable.ic_table_dinner,
                title = "Table Reservation",
                type = RestaurantSelection.TABLE_RESERVATION.name
            ),
            SelectionDataClass(
                image = R.drawable.ic_cost_estimation,
                title = "Cost Estimate",
                type = RestaurantSelection.COST_ESTIMATION.name
            ), SelectionDataClass(
                image = R.drawable.ic_receipt_bill,
                title = "Bill Payment",
                type = RestaurantSelection.TABLE_RESERVATION.name
            )
        )

        enum class RestaurantSelection {
            TABLE_MANAGEMENT,
            COST_ESTIMATION,
            TABLE_RESERVATION,
        }

    }
}