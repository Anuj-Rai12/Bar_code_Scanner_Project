package com.fbts.mpos.ui.searchfood.repo

import com.fbts.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface SearchFoodRepository {

    fun getItemMasterSync(stateNo: String): Flow<ApisResponse<out Any?>>

    fun getSearchFoodItem(query: String): Flow<ApisResponse<out Any?>>
    fun getSearchFoodItem(): Flow<ApisResponse<out Any?>>
}