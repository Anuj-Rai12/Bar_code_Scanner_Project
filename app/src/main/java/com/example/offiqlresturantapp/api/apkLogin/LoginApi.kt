package com.example.offiqlresturantapp.api.apkLogin

import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApKLoginPost
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApkLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {

    @Headers("SOAPAction: urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:Login")
    @POST("LoginAndGetMasterAPI")
    suspend fun sendApiPostRequest(
        @Body request: ApKLoginPost
    ): Response<ApkLoginResponse>
}