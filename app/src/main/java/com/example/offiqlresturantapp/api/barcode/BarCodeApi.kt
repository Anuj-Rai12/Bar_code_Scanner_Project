package com.example.offiqlresturantapp.api.barcode

import com.example.offiqlresturantapp.data.barcode.request.BarCodeRequest
import com.example.offiqlresturantapp.data.barcode.response.BarCodeResponse
import com.example.offiqlresturantapp.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BarCodeApi {

    @Headers("SOAPAction: ${AllStringConst.SoapAction.Barcode}")
    @POST(AllStringConst.End_point)
    suspend fun sendApiPostRequest(
        @Body request: BarCodeRequest
    ): Response<BarCodeResponse>

}