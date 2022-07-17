package com.example.mpos.api.barcode

import com.example.mpos.data.barcode.request.BarCodeRequest
import com.example.mpos.data.barcode.response.BarCodeResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BarCodeApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.Barcode}")
    @POST(AllStringConst.End_point)
    suspend fun sendApiPostRequest(
        @Body request: BarCodeRequest
    ): Response<BarCodeResponse>

}