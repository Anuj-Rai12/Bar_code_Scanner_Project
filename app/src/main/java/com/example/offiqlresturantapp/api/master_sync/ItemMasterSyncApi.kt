package com.example.offiqlresturantapp.api.master_sync

import com.example.offiqlresturantapp.data.item_master_sync.ItemMasterSyncRequest
import com.example.offiqlresturantapp.data.item_master_sync.ItemMasterSyncResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ItemMasterSyncApi {

    @Headers("SOAPAction: ${AllStringConst.SoapAction.itemMasterSync}")
    @POST(AllStringConst.End_point)
    suspend fun getItemMasterSync(@Body body: ItemMasterSyncRequest)
            : Response<ItemMasterSyncResponse>

}