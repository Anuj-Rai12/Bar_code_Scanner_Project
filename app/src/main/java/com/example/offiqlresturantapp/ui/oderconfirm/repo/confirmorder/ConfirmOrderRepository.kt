package com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmorder

import com.example.offiqlresturantapp.data.confirmOrder.ConfirmOrderRequest
import com.example.offiqlresturantapp.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface ConfirmOrderRepository {

    fun saveUserOrderItem(confirmOrderRequest: ConfirmOrderRequest): Flow<ApisResponse<out Any?>>

}