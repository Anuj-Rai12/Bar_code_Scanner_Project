package com.fbts.mpos.ui.oderconfirm.repo.confirmdining

import com.fbts.mpos.data.cofirmDining.ConfirmDiningRequest
import com.fbts.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface ConfirmDiningRepository {

    fun updateAndLockTbl(confirmDiningRequest: ConfirmDiningRequest):
            Flow<ApisResponse<out Any?>>

}