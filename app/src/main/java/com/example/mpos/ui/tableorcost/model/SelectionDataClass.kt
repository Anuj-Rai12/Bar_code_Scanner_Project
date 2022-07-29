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

        enum class RestaurantSelection {
            TABLEMGT,
            TABLERESERVATION,
            ESTIMATION,
            BILLING,
        }

    }

}