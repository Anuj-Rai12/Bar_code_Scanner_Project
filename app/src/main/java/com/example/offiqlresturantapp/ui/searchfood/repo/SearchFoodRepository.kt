package com.example.offiqlresturantapp.ui.searchfood.repo

import com.example.offiqlresturantapp.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface SearchFoodRepository {

    fun getItemMasterSync(stateNo: String): Flow<ApisResponse<out Any?>>
}