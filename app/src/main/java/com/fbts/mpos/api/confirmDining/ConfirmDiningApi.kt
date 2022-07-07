package com.fbts.mpos.api.confirmDining

import com.fbts.mpos.data.cofirmDining.ConfirmDiningRequest
import com.fbts.mpos.data.cofirmDining.response.ConfirmDiningSuccessResponse
import com.fbts.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ConfirmDiningApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.confirmDining}")
    @POST(AllStringConst.End_point)
    suspend fun setPostRequestApi(
        @Body request: ConfirmDiningRequest
    ): Response<ConfirmDiningSuccessResponse>

}