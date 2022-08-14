package com.example.mpos.ui.login.repo

import android.util.Log
import com.example.mpos.api.apkLogin.LoginApi
import com.example.mpos.data.login.model.api.ApKLoginPost
import com.example.mpos.data.login.model.api.json.ApkLoginJsonResponse
import com.example.mpos.data.logoutstaff.LogOutRequest
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.utils.*
import kotlinx.coroutines.Dispatchers.IO
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
                        RestaurantSingletonCls.getInstance().setRestaurantName(it.storeName)
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


    fun getLogOutResponse(request: LogOutRequest) = flow {
        emit(ApisResponse.Loading("Loading Profile..."))
        val res =
            try {
                val response = apkLoginApi.getLoOutStaff(request)
                if (response.isSuccessful) {
                    response.body()?.body?.value?.let { res ->
                        if (res.startsWith("02")) {
                            ApisResponse.Success(null)
                        } else {
                            ApisResponse.Error("User StaffId Don't Exist", null)
                        }
                    } ?: ApisResponse.Error("Oops cannot load Staff Info", null)
                } else {
                    ApisResponse.Error("Oops cannot find Staff Info", null)
                }
            } catch (e: Exception) {
                ApisResponse.Error(null, e)
            }
        emit(res)
    }.flowOn(IO)


}