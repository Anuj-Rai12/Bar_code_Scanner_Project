package com.example.offiqlresturantapp.repo


import android.util.Log
import com.example.offiqlresturantapp.TAG
import com.example.offiqlresturantapp.api.ApiInterface
import com.example.offiqlresturantapp.model.test.apklogin.EnvelopePostItem
import com.example.offiqlresturantapp.utils.ApiPostResponseObj
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun getResponse(post: EnvelopePostItem) = flow {
        emit("Loading Data")
        val data = try {
            val hashMap = hashMapOf<String, String>()
            hashMap["Authorization"] = ApiPostResponseObj.authHeader
            hashMap["SOAPAction"] = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:APKlogin"
            hashMap["Content-Type"] = "application/xml"
            hashMap["Accept"]="*/*"
            Log.i(TAG, "getResponse HAS_MAP: $hashMap")
            val request = apiInterface.sendApiPostRequest(hashMap,post)
            if (request.isSuccessful) {
                Log.i(TAG, "getResponse Body: ${request.body()}")
                request.body()
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


    fun getItemResponse() = flow {
        emit("Loading Data")
        val data = try {

            val hashMap = hashMapOf<String, String>()
            hashMap["Authorization"] = ApiPostResponseObj.authHeader
            hashMap["SOAPAction"] =
                "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:ItemMasterSync"
            hashMap["Content-Type"] = "application/xml"

            Log.i(TAG, "getResponse HAS_MAP: $hashMap")
            val request = apiInterface.getApiPostRequest(hashMap)

            if (request.isSuccessful) {
                Log.i(TAG, "getResponse Body: ${request.body()}")
                request.body()
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