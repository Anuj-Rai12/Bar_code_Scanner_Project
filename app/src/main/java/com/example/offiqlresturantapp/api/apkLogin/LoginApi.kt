package com.example.offiqlresturantapp.api.apkLogin

import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.ApkLoginResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {

    @Headers("SOAPAction: ${AllStringConst.SoapAction.ApkLogin}")
    @POST(AllStringConst.End_point)
    suspend fun sendApiPostRequest(
        @Body request: ApKLoginPost
    ): Response<ApkLoginResponse>
}