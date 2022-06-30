package com.example.offiqlresturantapp.api.confirmOrder

import com.example.offiqlresturantapp.data.confirmOrder.ConfirmOrderRequest
import com.example.offiqlresturantapp.data.confirmOrder.response.ConfirmOrderSuccessResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ConfirmOrderApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.confirmOrder}")
    @POST(AllStringConst.End_point)
    suspend fun sendPostRequestApi(
        @Body request: ConfirmOrderRequest
    ): Response<ConfirmOrderSuccessResponse>

}