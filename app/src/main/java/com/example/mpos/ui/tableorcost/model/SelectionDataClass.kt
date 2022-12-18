package com.example.mpos.ui.tableorcost.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class SelectionDataClass(
    val image: Int,
    val title: String,
    val type: String,
    val dynamicMenuEnable: Boolean,
    val isBarcodeVisible:Boolean
) : Parcelable {
    companion object {
        fun generateData(title: String, img: Int, type: String,dynamicMenuEnable:Boolean,isBarcodeVisible:Boolean): SelectionDataClass {
            return SelectionDataClass(
                image = img,
                title = title,
                type = type,
                dynamicMenuEnable = dynamicMenuEnable,
                isBarcodeVisible=isBarcodeVisible
            )
        }

        enum class RestaurantSelection {
            TABLEMGT,
            TABLERESERVATION,
            ESTIMATION,
            BILLING,
            SHOWROOMESTIMATE,
            RESTAURANTESTIMATE,
            SHOWROOMBILLING,
            RESTAURANTBILLING
        }

        const val TABLE_MGT = "Table Management"
        const val TABLERESERVATION = "Table Reservation"
        const val ESTIMATION = "Cost Estimate"
        const val BILLING = "Bill Payment"
        const val SHOWROOMESTIMATE = "Showroom Estimation"
        const val RESTAURANTESTIMATE = "Restaurant Estimation"
        const val RESTAURANTBILLING = "Restaurant Billing"
        const val SHOWROOMBILLING = "Showroom Billing"

    }

}