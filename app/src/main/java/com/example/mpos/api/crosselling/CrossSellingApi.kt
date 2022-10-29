package com.example.mpos.api.crosselling

import com.example.mpos.data.crosssellingApi.request.CrossSellingRequest
import com.example.mpos.data.crosssellingApi.response.CrossSellingResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CrossSellingApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.crossSelling}")
    @POST(AllStringConst.End_point)
    suspend fun crossSellingAPI(
        @Body request: CrossSellingRequest
    ): Response<CrossSellingResponse>

}