package com.example.offiqlresturantapp.ui.testingconnection.repo

import android.util.Log
import com.example.offiqlresturantapp.api.apkLogin.LoginApi
import com.example.offiqlresturantapp.api.testconnection.TestConnectionApi
import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.data.testconnection.TestingConnection
import com.example.offiqlresturantapp.data.testconnection.api.RequestConnectionBody
import com.example.offiqlresturantapp.data.testconnection.api.TestingConnectionRequest
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.di.RetrofitInstance
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import java.util.*

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

    private val error = "Please Check the Credentials or Try Again!!"
    private val errorApi = "Oops SomeTing Went Wrong Try Again!!"

    fun addTestingUrl(testingConnection: TestingConnection) = flow {
        emit(ApisResponse.Loading("Please Wait.."))
        val reqData =
            TestingConnectionRequest(
                RequestConnectionBody(testingConnection.storeId.uppercase(Locale.getDefault()))
            )

        val data = try {
            val auth = AllStringConst
                .getAuthHeader(genToken("${testingConnection.userId}:${testingConnection.passId}"))
            val retrofit = RetrofitInstance(auth, testingConnection.baseUrl)
            val api = buildApi<TestConnectionApi>(retrofit.getRetrofit())
            val response = api.getTestConnectionApi(reqData)
            val finalRes = if (response.isSuccessful) {
                response.body()?.body?.value?.let { res ->
                    return@let if (res.lowercase(Locale.getDefault()).toBoolean()) {
                        userSoredData.updateBaseInfo(
                            mainUrl = testingConnection.baseUrl,
                            userId = testingConnection.userId,
                            password = testingConnection.passId,
                            storeId = testingConnection.storeId.uppercase(Locale.getDefault())
                        )
                        ApisResponse.Success(null)
                    } else {
                        ApisResponse.Error(
                            "${getEmojiByUnicode(0x1F3EC)} Store ID is Wrong please ${
                                getEmojiByUnicode(
                                    0x274C
                                )
                            } Please check it and Try Again!!",
                            null
                        )
                    }
                } ?: ApisResponse.Error(error, null)
            } else {
                ApisResponse.Error(errorApi, null)
            }
            finalRes
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}