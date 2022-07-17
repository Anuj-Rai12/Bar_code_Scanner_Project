package com.example.mpos.ui.oderconfirm.repo.confirmdining

import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface ConfirmDiningRepository {

    fun updateAndLockTbl(confirmDiningRequest: ConfirmDiningRequest):
            Flow<ApisResponse<out Any?>>

}