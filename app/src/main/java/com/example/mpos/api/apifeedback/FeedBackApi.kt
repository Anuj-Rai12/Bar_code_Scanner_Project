package com.example.mpos.api.apifeedback

import com.example.mpos.data.feedback.feedbck.FeedBackRequest
import com.example.mpos.data.feedback.feedbck.FeedBackResponse
import com.example.mpos.data.feedback.invoice.FinalInvoiceSendRequest
import com.example.mpos.data.feedback.invoice.FinalInvoiceSendResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FeedBackApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey} ${AllStringConst.SoapAction.finalFeedBackSend}")
    @POST(AllStringConst.End_point)
    suspend fun sendFeedBackApi(
        @Body request: FeedBackRequest
    ): Response<FeedBackResponse>


    @Headers("${AllStringConst.SoapAction.HeaderKey} ${AllStringConst.SoapAction.finalInvoiceSend}")
    @POST(AllStringConst.End_point)
    suspend fun sendFeedBackInvoiceApi(
        @Body requestBody: FinalInvoiceSendRequest
    ): Response<FinalInvoiceSendResponse>


}