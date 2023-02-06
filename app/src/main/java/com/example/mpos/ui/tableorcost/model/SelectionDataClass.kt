package com.example.mpos.ui.tableorcost.model

import android.os.Parcelable
import com.example.mpos.data.login.model.api.json.ApkLoginJsonResponse
import kotlinx.parcelize.Parcelize


@Parcelize
data class SelectionDataClass(
    val image: Int,
    val title: String,
    val type: String,
    val dynamicMenuEnable: Boolean,
    val isBarcodeVisible: Boolean,
    val uPICode: String,
    val billingFromEDC: Boolean,
    val paymentLs: List<String>,
    val apk:ApkLoginJsonResponse
) : Parcelable {
    companion object {
        fun generateData(
            title: String,
            img: Int,
            type: String,
            dynamicMenuEnable: Boolean,
            isBarcodeVisible: Boolean,
            upi: String,
            billingFromEDC: Boolean,
            paymentLs: List<String>,
            apk: ApkLoginJsonResponse
        ): SelectionDataClass {
            return SelectionDataClass(
                image = img,
                title = title,
                type = type,
                dynamicMenuEnable = dynamicMenuEnable,
                isBarcodeVisible = isBarcodeVisible,
                uPICode = upi,
                billingFromEDC = billingFromEDC,
                paymentLs = paymentLs,
                apk =apk
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