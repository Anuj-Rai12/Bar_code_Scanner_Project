package com.example.mpos.ui.searchfood.repo

import com.example.mpos.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface SearchFoodRepository {

    fun getItemMasterSync(stateNo: String,screenType:String?=null,isLoad:Boolean): Flow<ApisResponse<out Any?>>

    fun getSearchFoodItem(query: String): Flow<ApisResponse<out Any?>>
    fun getSearchFoodItem(): Flow<ApisResponse<out Any?>>

    fun getCrossSellingResponse(itemCode: String,count:Int): Flow<ApisResponse<out Any?>>

    fun getItemQtySize(url:String,auth:String,storeId:String,itemCode: String): Flow<ApisResponse<out Any?>>

}