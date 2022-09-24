package com.example.mpos.data.billing.printInvoice.json


import com.google.gson.annotations.SerializedName

data class GstDetail(
    @SerializedName("CGSTAmt") val cGSTAmt: String,
    @SerializedName("CessAmt") val cessAmt: String,
    @SerializedName("GstPer") val gstPer: String,
    @SerializedName("IGSTAmt") val iGSTAmt: String,
    @SerializedName("SGSTAmt") val sGSTAmt: String
)