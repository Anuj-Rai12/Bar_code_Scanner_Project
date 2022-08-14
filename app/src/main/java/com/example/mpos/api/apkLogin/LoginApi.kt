package com.example.mpos.api.apkLogin

import com.example.mpos.data.login.model.api.ApKLoginPost
import com.example.mpos.data.login.model.api.ApkLoginResponse
import com.example.mpos.data.logoutstaff.LogOutRequest
import com.example.mpos.data.logoutstaff.LogOutResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey} ${AllStringConst.SoapAction.ApkLogin}")
    @POST(AllStringConst.End_point)
    suspend fun sendApiPostRequest(
        @Body request: ApKLoginPost
    ): Response<ApkLoginResponse>


    @Headers("${AllStringConst.SoapAction.HeaderKey} ${AllStringConst.SoapAction.logoutStaff}")
    @POST(AllStringConst.End_point)
    suspend fun getLoOutStaff(
        @Body requestBody: LogOutRequest
    ): Response<LogOutResponse>



}