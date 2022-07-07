package com.fbts.mpos.api.apkLogin

import com.fbts.mpos.data.login.model.api.ApKLoginPost
import com.fbts.mpos.data.login.model.api.ApkLoginResponse
import com.fbts.mpos.utils.AllStringConst
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
}