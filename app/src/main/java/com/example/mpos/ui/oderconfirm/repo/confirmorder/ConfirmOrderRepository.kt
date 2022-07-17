package com.example.mpos.ui.oderconfirm.repo.confirmorder

import com.example.mpos.data.confirmOrder.ConfirmOrderRequest
import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface ConfirmOrderRepository {

    fun saveUserOrderItem(confirmOrderRequest: ConfirmOrderRequest): Flow<ApisResponse<out Any?>>

}