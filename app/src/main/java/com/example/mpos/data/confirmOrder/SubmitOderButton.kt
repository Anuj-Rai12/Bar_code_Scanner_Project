package com.example.mpos.data.confirmOrder

import com.example.mpos.R

data class SubmitOderButton(
    val title: String,
    val src: Int,
    val orderType: String
) {
    companion object {
        val list = listOf(
            SubmitOderButton(
                title = "Confirm Order",
                src = R.drawable.dinner_img,
                orderType = "CONFIRM_ORDER"
            ), SubmitOderButton(
                orderType = "PAYMENT",
                title = "Proceed to Payment",
                src = R.drawable.ruppe_payment
            )
        )
    }
}