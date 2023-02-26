package com.example.mpos.data.billing.printInvoice.json

import com.google.gson.annotations.SerializedName

data class VatDetail(
    @SerializedName("VatPer") val vatPer: String,
    @SerializedName("VatBaseAmt") val vatBaseAmt: String,
    @SerializedName("VatAmt") val vatAmt: String
)