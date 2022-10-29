package com.example.mpos.ui.searchfood.repo

import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface SearchFoodRepository {

    fun getItemMasterSync(stateNo: String): Flow<ApisResponse<out Any?>>

    fun getSearchFoodItem(query: String): Flow<ApisResponse<out Any?>>
    fun getSearchFoodItem(): Flow<ApisResponse<out Any?>>

    fun getCrossSellingResponse(itemCode: String): Flow<ApisResponse<out Any?>>

}