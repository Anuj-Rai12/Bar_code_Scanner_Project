package com.fbts.mpos.api.master_sync

import com.fbts.mpos.data.item_master_sync.ItemMasterSyncRequest
import com.fbts.mpos.data.item_master_sync.ItemMasterSyncResponse
import com.fbts.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ItemMasterSyncApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.itemMasterSync}")
    @POST(AllStringConst.End_point)
    suspend fun getItemMasterSync(@Body body: ItemMasterSyncRequest)
            : Response<ItemMasterSyncResponse>

}