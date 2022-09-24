package com.example.mpos.data.billing.printInvoice.json


import com.google.gson.annotations.SerializedName

data class PaymentDetail(
    @SerializedName("Amt") val amt: String,
    @SerializedName("TenderType") val tenderType: String
)