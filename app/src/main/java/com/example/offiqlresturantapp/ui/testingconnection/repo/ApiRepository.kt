package com.example.offiqlresturantapp.ui.testingconnection.repo

import android.util.Log
import com.example.offiqlresturantapp.api.apkLogin.LoginApi
import com.example.offiqlresturantapp.ui.testingconnection.model.api.ApKLoginPost
import com.example.offiqlresturantapp.ui.testingconnection.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.buildApi
import com.example.offiqlresturantapp.utils.deserializeFromJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject

class ApiRepository @Inject constructor(retrofit: Retrofit) {

    private val apkLoginApi = buildApi<LoginApi>(retrofit)

    fun getApkLoginResponse(requestBody: ApKLoginPost) = flow {
        emit(ApisResponse.Loading("Loading.."))
        val data = try {
            val response = apkLoginApi.sendApiPostRequest(requestBody)
            val info = if (response.isSuccessful) {
                deserializeFromJson<ApkLoginJsonResponse>(response.body()?.apkLoginResult?.value)
            } else {
                Log.i(TAG, "getApkLoginResponse: ${response.errorBody()}")
                null
            }
            ApisResponse.Success(info)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}