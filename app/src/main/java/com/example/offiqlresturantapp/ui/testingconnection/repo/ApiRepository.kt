package com.example.offiqlresturantapp.ui.testingconnection.repo

import android.util.Log
import com.example.offiqlresturantapp.api.apkLogin.LoginApi
import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.ApkBody
import com.example.offiqlresturantapp.data.login.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.data.testconnection.TestingConnection
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class ApiRepository constructor(
    retrofit: Retrofit,
    private val userSoredData: UserSoredData
) {

    private val apkLoginApi = buildApi<LoginApi>(retrofit)

    fun getApkLoginResponse(requestBody: ApKLoginPost, flag: Boolean) = flow {
        emit(ApisResponse.Loading("Loading.."))
        val data = try {
            val response = apkLoginApi.sendApiPostRequest(requestBody)
            val info = if (response.isSuccessful) {
                deserializeFromJson<ApkLoginJsonResponse>(response.body()?.apkLoginResult?.value)?.let {
                    if (it.status && flag) {
                        userSoredData.updateInfo(
                            userId = requestBody.apK?.userID!!,
                            password = requestBody.apK.password!!,
                        )
                        RestaurantSingletonCls.getInstance().setStoreId(requestBody.apK.storeNo!!)
                    }
                    return@let it
                }
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


    fun addTestingUrl(testingConnection: TestingConnection) = flow {
        emit(ApisResponse.Loading("Please Wait.."))
        val reqData =
            ApKLoginPost(ApkBody(testingConnection.storeId, "testingConnection", "somePassword"))
        val data = try {
            val auth = AllStringConst
                .getAuthHeader(genToken("${testingConnection.userId}:${testingConnection.passId}"))
            val retrofit = RetrofitInstance(auth, testingConnection.baseUrl)
            val api = buildApi<LoginApi>(retrofit.getRetrofit())
            val response = api.sendApiPostRequest(reqData)
            val finalRes = if (response.isSuccessful) {
                deserializeFromJson<ApkLoginJsonResponse>(response.body()?.apkLoginResult?.value)?.let {
                    userSoredData.updateBaseInfo(
                        mainUrl = testingConnection.baseUrl,
                        userId = testingConnection.userId,
                        password = testingConnection.passId,
                        storeId = testingConnection.storeId
                    )
                    return@let ApisResponse.Success("ok")
                } ?: ApisResponse.Error(null, null)
            } else {
                ApisResponse.Error(null, null)
            }
            finalRes
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}