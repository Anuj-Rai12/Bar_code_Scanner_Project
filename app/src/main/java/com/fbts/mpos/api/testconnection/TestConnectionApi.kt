package com.fbts.mpos.api.testconnection

import com.fbts.mpos.data.testconnection.api.TestingConnectionRequest
import com.fbts.mpos.data.testconnection.api.TestingConnectionResponse
import com.fbts.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TestConnectionApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.testConnection}")
    @POST(AllStringConst.End_point)
    suspend fun getTestConnectionApi(@Body request: TestingConnectionRequest): Response<TestingConnectionResponse>
}