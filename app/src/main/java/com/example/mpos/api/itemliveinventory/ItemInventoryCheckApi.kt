package com.example.mpos.api.itemliveinventory

import com.example.mpos.data.inventorycheck.ItemInventoryCheckRequest
import com.example.mpos.data.inventorycheck.ItemInventoryResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ItemInventoryCheckApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.itemLiveInventory}")
    @POST(AllStringConst.End_Point_ItemInventory)
    suspend fun getItemLiveInventorySync(@Body body: ItemInventoryCheckRequest)
            : Response<ItemInventoryResponse>

}