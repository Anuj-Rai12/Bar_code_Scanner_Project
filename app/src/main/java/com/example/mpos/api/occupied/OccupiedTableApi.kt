package com.example.mpos.api.occupied

import com.example.mpos.data.occupied.OccupiedTableRequest
import com.example.mpos.data.occupied.OccupiedTableResponse
import com.example.mpos.utils.AllStringConst
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