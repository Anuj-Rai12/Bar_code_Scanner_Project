package com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmdining

import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface ConfirmDiningRepository {

    fun updateAndLockTbl(confirmDiningRequest: ConfirmDiningRequest):
            Flow<ApisResponse<out Any?>>

}