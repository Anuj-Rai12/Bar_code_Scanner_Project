package com.fbts.mpos.ui.oderconfirm.repo.confirmorder

import com.fbts.mpos.data.confirmOrder.ConfirmOrderRequest
import com.fbts.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface ConfirmOrderRepository {

    fun saveUserOrderItem(confirmOrderRequest: ConfirmOrderRequest): Flow<ApisResponse<out Any?>>

}