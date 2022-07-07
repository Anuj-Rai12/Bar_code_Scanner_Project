package com.fbts.mpos.api.occupied

import com.fbts.mpos.data.occupied.OccupiedTableRequest
import com.fbts.mpos.data.occupied.OccupiedTableResponse
import com.fbts.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OccupiedTableApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.occupiedTbl}")
    @POST(AllStringConst.End_point)
    suspend fun sendPostRequestApi(
        @Body request: OccupiedTableRequest
    ): Response<OccupiedTableResponse>

}