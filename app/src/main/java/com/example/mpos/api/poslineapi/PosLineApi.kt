package com.example.mpos.api.poslineapi

import com.example.mpos.data.poslineitem.request.PosLineItemApiRequest
import com.example.mpos.data.poslineitem.response.PosLineItemApiResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PosLineApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.PosLineItem}")
    @POST(AllStringConst.End_point)
    suspend fun sendPostRequest(
        @Body posLineItemApiRequest: PosLineItemApiRequest
    ): Response<PosLineItemApiResponse>

}