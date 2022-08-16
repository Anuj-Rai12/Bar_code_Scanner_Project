package com.example.mpos.api.deal

import com.example.mpos.data.deals.DealsRequest
import com.example.mpos.data.deals.DealsResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DealsApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.getDeals}")
    @POST(AllStringConst.End_point)
    suspend fun getDeals(@Body response: DealsRequest): Response<DealsResponse>
}