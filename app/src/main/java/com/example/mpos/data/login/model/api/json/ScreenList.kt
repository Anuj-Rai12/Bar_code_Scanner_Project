package com.example.mpos.data.login.model.api.json


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenList(
    @SerializedName("DynamicMenuEnable") val dynamicMenuEnable: Boolean,
    @SerializedName("BillingFromEDC") val billingFromEDC: Boolean,
    @SerializedName("screencaption") val screenCaption: String,
    @SerializedName("UPICode") val uPICode: String,
    @SerializedName("ModernSearch") val modernSearch: Boolean,
    @SerializedName("EnableCustDetail") val enableCustDetail: Boolean,
    @SerializedName("screenlist") val screenList: String,
    @SerializedName("KOTPrintFromEDC") val kotPrintFromEDC: Boolean,
    @SerializedName("Estimateprint") val estimatePrint: Boolean,
    @SerializedName("EstimatePrintcount") val estimatePrintCount: Int,
    @SerializedName("PaymentType") val paymentLs: List<String>
) : Parcelable