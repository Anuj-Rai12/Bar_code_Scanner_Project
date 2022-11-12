package com.example.mpos.data.billing.printInvoice.json


import com.google.gson.annotations.SerializedName

data class PrintInvoice(
    @SerializedName("AmtIncGST") val amtIncGST: String,
    @SerializedName("BaseAmount") val baseAmount: String,
    @SerializedName("BillType") val billType: String,
    @SerializedName("BillTypeTxt") val billTypeTxt: String,
    @SerializedName("childitemList") val childitemList: List<Childitem>,
    @SerializedName("DiscountAmt") val discountAmt: String,
    @SerializedName("FooterTxt1") val footerTxt1: String,
    @SerializedName("FooterTxt2") val footerTxt2: String,
    @SerializedName("FooterTxt3") val footerTxt3: String,
    @SerializedName("FooterTxt4") val footerTxt4: String,
    @SerializedName("FooterTxt5") val footerTxt5: String,
    @SerializedName("FooterTxt6") val footerTxt6: String,
    @SerializedName("FooterTxt7") val footerTxt7: String,
    @SerializedName("GstDetails") val gstDetails: List<GstDetail>,
    @SerializedName("HeaderTxt1") val headerTxt1: String,
    @SerializedName("HeaderTxt2") val headerTxt2: String,
    @SerializedName("HeaderTxt3") val headerTxt3: String,
    @SerializedName("HeaderTxt4") val headerTxt4: String,
    @SerializedName("HeaderTxt5") val headerTxt5: String,
    @SerializedName("HeaderTxt6") val headerTxt6: String,
    @SerializedName("HeaderTxt7") val headerTxt7: String,
    @SerializedName("PaymentDetails") val paymentDetails: List<PaymentDetail>,
    @SerializedName("QRPrint") val qrPrint: List<QrPrint>,
    @SerializedName("RoundAmt") val roundAmt: String,
    @SerializedName("SubHeaderTxt1") val subHeaderTxt1: String,
    @SerializedName("SubHeaderTxt2") val subHeaderTxt2: String,
    @SerializedName("SubHeaderTxt3") val subHeaderTxt3: String,
    @SerializedName("SubHeaderTxt4") val subHeaderTxt4: String,
    @SerializedName("SubHeaderTxt5") val subHeaderTxt5: String
)