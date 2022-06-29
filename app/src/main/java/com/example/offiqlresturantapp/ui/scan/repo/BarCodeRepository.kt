package com.example.offiqlresturantapp.ui.scan.repo

import android.util.Log
import com.example.offiqlresturantapp.api.barcode.BarCodeApi
import com.example.offiqlresturantapp.data.barcode.request.BarCodeRequest
import com.example.offiqlresturantapp.data.barcode.response.json.BarcodeJsonResponse
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.TAG
import com.example.offiqlresturantapp.utils.buildApi
import com.example.offiqlresturantapp.utils.deserializeFromJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import retrofit2.Retrofit

class BarCodeRepository(retrofit: Retrofit) {
    val api = buildApi<BarCodeApi>(retrofit)

    private val err = "Cannot Find Item Try Another!!"
    private val loading = "Checking for Item.."

    fun getBarCodeResponse(barCodeRequest: BarCodeRequest) = flow {
        emit(ApisResponse.Loading(loading))
        val data = try {
            val response = api.sendApiPostRequest(barCodeRequest)
            if (response.isSuccessful) {
                deserializeFromJson<BarcodeJsonResponse>(response.body()?.barCodeBody?.value)?.let { json ->
                    ApisResponse.Success(json)
                } ?: ApisResponse.Error(err, null)
            } else {
                Log.i(TAG, "getBarCodeResponse: ${response.errorBody()?.string()}")
                ApisResponse.Error(err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

}