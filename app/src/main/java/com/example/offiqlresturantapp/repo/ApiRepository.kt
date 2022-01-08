package com.example.offiqlresturantapp.repo


import android.util.Log
import com.example.offiqlresturantapp.TAG
import com.example.offiqlresturantapp.api.ApiInterface
import com.example.offiqlresturantapp.model.test.apkjanitemmastersync.EnvelopeItemSync
import com.example.offiqlresturantapp.model.test.apkjanitemmastersync.ItemMasterSyncJan
import com.example.offiqlresturantapp.model.test.apkJanLogin.EnvelopeOption
import com.example.offiqlresturantapp.model.test.apkJanLogin.jsonfol.ResultApiLoginCls
import com.example.offiqlresturantapp.model.test.apkjanitemmastersync.jsonfol.ItemMasterJsonResult
import com.example.offiqlresturantapp.othermodel.MyApiInterface
import com.example.offiqlresturantapp.othermodel.RssApiInterface
import com.example.offiqlresturantapp.utils.ApiPostResponseObj
import com.example.offiqlresturantapp.utils.Helper
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiInterface: ApiInterface,
    private val myApiInterface: MyApiInterface,
    private val rssApiInterface: RssApiInterface
) {

    fun getMyRssApiResponse() = flow {
        emit("Loading Data")
        val data = try {

            val response = rssApiInterface.getRssFeed()
            if (response.isSuccessful) {
                Log.i(TAG, "getMyApiResponse: ${response.body()?.channel?.item?.last()}")
                response.body().toString()
            } else {
                Log.i(TAG, "getMyApiResponse Error: ${response.errorBody()}")
                response.code().toString()
            }
        } catch (e: Exception) {
            e.localizedMessage
        } catch (e: HttpException) {
            e.localizedMessage
        }
        emit(data)
    }




    fun getMyApiResponse() = flow {
        emit("Loading Data")
        val data = try {

            val response = myApiInterface.sendApiInterface()
            if (response.isSuccessful) {
                Log.i(TAG, "getMyApiResponse: ${response.body()}")
                response.body().toString()
            } else {
                Log.i(TAG, "getMyApiResponse Error: ${response.errorBody()}")
                response.code().toString()
            }

        } catch (e: Exception) {
            e.localizedMessage
        } catch (e: HttpException) {
            e.localizedMessage
        }
        emit(data)
    }


    fun getResponse(post: EnvelopeOption) = flow {
        emit("Loading Data")
        val data = try {
            val hashMap = hashMapOf<String, String>()
            hashMap["Authorization"] = ApiPostResponseObj.authHeader
            hashMap["SOAPAction"] =
                "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:APKlogin"
            hashMap["Content-Type"] = "application/xml"

            //hashMap["Accept"] = "*/*"
            Log.i(TAG, "getResponse HAS_MAP: $hashMap")
            val request = apiInterface.sendApiPostRequest(hashMap, post)
            if (request.isSuccessful) {
                Log.i(TAG, "getResponse Body: ${request.body()}")
                Helper.deserializeFromJson<ResultApiLoginCls>(request.body()?.apkLoginResult?.value)
            } else {
                Log.i(TAG, "getResponse Error Body: ${request.errorBody()}")
                Log.i(TAG, "getResponse : $request")
                null
            }

        } catch (e: Exception) {
            Log.i(TAG, "getResponse: $e.message")
            e.message
        }
        emit(data)
    }


    fun getItemResponse() = flow {
        emit("Loading Data")
        val data = try {

            val hashMap = hashMapOf<String, String>()
            hashMap["Authorization"] = ApiPostResponseObj.authHeader
            hashMap["SOAPAction"] =
                "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:ItemMasterSync"
            hashMap["Content-Type"] = "application/xml"

            Log.i(TAG, "getResponse HAS_MAP: $hashMap")
            val request = apiInterface.getApiPostRequest(hashMap, EnvelopeItemSync(ItemMasterSyncJan()))

            if (request.isSuccessful) {
                Log.i(TAG, "getResponse Body: ${request.body()}")
                Helper.deserializeFromJson<ItemMasterJsonResult>(request.body()?.apkLoginResult?.value)
            } else {
                Log.i(TAG, "getResponse Error Body: ${request.errorBody()}")
                Log.i(TAG, "getResponse : $request")
                null
            }

        } catch (e: Exception) {
            e.localizedMessage
        } catch (e: HttpException) {
            e.localizedMessage
        }
        emit(data)
    }

}