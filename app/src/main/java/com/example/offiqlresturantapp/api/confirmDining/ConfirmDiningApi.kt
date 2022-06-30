package com.example.offiqlresturantapp.api.confirmDining

import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.data.cofirmDining.response.ConfirmDiningSuccessResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ConfirmDiningApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.confirmDining}")
    @POST(AllStringConst.End_point)
    suspend fun setPostRequestApi(
        @Body request: ConfirmDiningRequest
    ): Response<ConfirmDiningSuccessResponse>

}