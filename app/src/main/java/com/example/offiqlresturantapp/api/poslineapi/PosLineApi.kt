package com.example.offiqlresturantapp.api.poslineapi

import com.example.offiqlresturantapp.data.poslineitem.request.PosLineItemApiRequest
import com.example.offiqlresturantapp.data.poslineitem.response.PosLineItemApiResponse
import com.example.offiqlresturantapp.utils.AllStringConst
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