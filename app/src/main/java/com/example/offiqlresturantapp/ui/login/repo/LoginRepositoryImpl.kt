package com.example.offiqlresturantapp.ui.login.repo

import android.util.Log
import com.example.offiqlresturantapp.api.apkLogin.LoginApi
import com.example.offiqlresturantapp.data.login.model.api.ApKLoginPost
import com.example.offiqlresturantapp.data.login.model.api.json.ApkLoginJsonResponse
import com.example.offiqlresturantapp.dataStore.UserSoredData
import com.example.offiqlresturantapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class LoginRepositoryImpl(
    retrofit: Retrofit,
    private val userSoredData: UserSoredData
) {

    private val apkLoginApi = buildApi<LoginApi>(retrofit)

    fun getApkLoginResponse(requestBody: ApKLoginPost, flag: Boolean) = flow {
        emit(ApisResponse.Loading("Please Wait"))
        val data = try {
            val response = apkLoginApi.sendApiPostRequest(requestBody)
            val info = if (response.isSuccessful) {
                deserializeFromJson<ApkLoginJsonResponse>(response.body()?.apkLoginResult?.value)?.let {
                    if (it.status && flag) {
                        userSoredData.updateInfo(
                            userId = requestBody.apK?.userID!!,
                            password = requestBody.apK.password!!
                        )
                        RestaurantSingletonCls.getInstance().setStoreId(requestBody.apK.storeNo!!)
                        RestaurantSingletonCls.getInstance().setUserID(requestBody.apK.userID)
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
    }.flowOn(Dispatchers.IO)

}