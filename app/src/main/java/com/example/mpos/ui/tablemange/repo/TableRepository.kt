package com.example.mpos.ui.tablemange.repo

import com.example.mpos.utils.AllStringConst
import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface TableRepository {

    fun getTblInformation(
        storeId: String,
        type: String = AllStringConst.API.RESTAURANT.name
    ): Flow<ApisResponse<out Any?>>


}