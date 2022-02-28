package com.example.offiqlresturantapp.ui.tablemange.repo

import com.example.offiqlresturantapp.utils.AllStringConst
import com.example.offiqlresturantapp.utils.ApisResponse
import kotlinx.coroutines.flow.Flow

interface TableRepository {

    fun getTblInformation(
        storeId: String,
        type: String = AllStringConst.API.RESTAURANT.name
    ): Flow<ApisResponse<out Any?>>


}