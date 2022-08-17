package com.example.mpos.api.deal

import com.example.mpos.data.deals.DealsRequest
import com.example.mpos.data.deals.DealsResponse
import com.example.mpos.data.deals.confirmdeals.ConfirmDealsRequest
import com.example.mpos.data.deals.confirmdeals.ConfirmDealsResponse
import com.example.mpos.data.deals.scan_and_find_deals.ScanAndFindDealsRequest
import com.example.mpos.data.deals.scan_and_find_deals.ScanAndFindDealsResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DealsApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.getDeals}")
    @POST(AllStringConst.End_point)
    suspend fun getDeals(@Body response: DealsRequest): Response<DealsResponse>


    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.confirmDeals}")
    @POST(AllStringConst.End_point)
    suspend fun postDealsToApi(@Body response: ConfirmDealsRequest): Response<ConfirmDealsResponse>


    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.scanAndFindDeals}")
    @POST(AllStringConst.End_point)
    suspend fun getDealsItem(@Body response: ScanAndFindDealsRequest): Response<ScanAndFindDealsResponse>

}