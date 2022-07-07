package com.fbts.mpos.api.barcode

import com.fbts.mpos.data.barcode.request.BarCodeRequest
import com.fbts.mpos.data.barcode.response.BarCodeResponse
import com.fbts.mpos.utils.AllStringConst
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