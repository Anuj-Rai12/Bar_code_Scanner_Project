package com.fbts.mpos.ui.tablemange.repo

import com.fbts.mpos.utils.AllStringConst
import com.fbts.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface TableRepository {

    fun getTblInformation(
        storeId: String,
        type: String = AllStringConst.API.RESTAURANT.name
    ): Flow<ApisResponse<out Any?>>


}