package com.example.offiqlresturantapp.api.testconnection

import com.example.offiqlresturantapp.data.testconnection.api.TestingConnectionRequest
import com.example.offiqlresturantapp.data.testconnection.api.TestingConnectionResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TestConnectionApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.testConnection}")
    @POST(AllStringConst.End_point)
    suspend fun getTestConnectionApi(@Body request: TestingConnectionRequest): Response<TestingConnectionResponse>
}