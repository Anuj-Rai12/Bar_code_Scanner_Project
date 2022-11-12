package com.example.mpos.data.billing.printInvoice.json

import com.google.gson.annotations.SerializedName

data class QrPrint(
    @SerializedName("TenderType")
    val tenderType: String,
    @SerializedName("Amt")
    val amt: String,
)