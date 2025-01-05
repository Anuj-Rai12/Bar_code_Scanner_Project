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
    val kotPrintFromEDC: Boolean,
    val uPICode: String,
    val modernSearch: Boolean,
    val enableCustDetail: Boolean,
    val estimatePrint: Boolean,
    val estimatePrintcount: Int,
    val billingFromEDC: Boolean,
    val IsUpdateQty: Boolean,
    val freezePaymentwindow :Boolean,
    val paymentLs: List<String>,
    val enableBillingTableMgt: Boolean,
    val stockCheck: Boolean,
    val apk: ApkLoginJsonResponse,
    val stockCheckUrl:String,
    val stockCheckUser:String,
    val stockCheckPassword:String
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
            apk: ApkLoginJsonResponse,
            kotPrintFromEDC: Boolean,
            estimatePrint: Boolean,
            modernSearch: Boolean,
            enableCustDetail: Boolean,
            estimatePrintcount: Int,
            enableBillingTableMgt:Boolean,
            IsUpdateQty: Boolean,
            freezePaymentwindow:Boolean,
            stockCheck:Boolean,
            stockCheckUrl:String,
            stockCheckUser:String,
            stockCheckPassword:String,
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
                apk = apk,
                estimatePrint = estimatePrint,
                kotPrintFromEDC = kotPrintFromEDC,
                modernSearch = modernSearch,
                enableCustDetail = enableCustDetail,
                estimatePrintcount = estimatePrintcount,
                enableBillingTableMgt = enableBillingTableMgt,
                IsUpdateQty = IsUpdateQty,
                freezePaymentwindow = freezePaymentwindow,
                stockCheck = stockCheck,
                stockCheckUser = stockCheckUser,
                stockCheckPassword = stockCheckPassword,
                stockCheckUrl = stockCheckUrl
            )
        }

        enum class RestaurantSelection {
            TABLEMGT, TABLERESERVATION, ESTIMATION, BILLING, SHOWROOMESTIMATE, RESTAURANTESTIMATE, SHOWROOMBILLING, RESTAURANTBILLING
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