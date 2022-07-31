package com.example.mpos.api.confirmDining

import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.data.cofirmDining.response.ConfirmDiningSuccessResponse
import com.example.mpos.data.costestimation.request.CostEstimation
import com.example.mpos.data.costestimation.response.CostEstimationResponse
import com.example.mpos.utils.AllStringConst
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



    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.costEstimation}")
    @POST(AllStringConst.End_point)
    suspend fun setPostCostEstimationApi(
        @Body request: CostEstimation
    ): Response<CostEstimationResponse>


}