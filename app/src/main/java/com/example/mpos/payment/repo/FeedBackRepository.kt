package com.example.mpos.payment.repo

import com.example.mpos.api.apifeedback.FeedBackApi
import com.example.mpos.data.feedback.feedbck.FeedBackRequest
import com.example.mpos.data.feedback.invoice.FinalInvoiceSendRequest
import com.example.mpos.ui.menu.repo.MenuRepository
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class FeedBackRepository(val retrofit: Retrofit) {

    private val feedBackApi = buildApi<FeedBackApi>(retrofit)

    fun getFeedBackResponse(info: FeedBackRequest) = flow {
        emit(ApisResponse.Loading("Sending FeedBack.."))
        val data = try {
            val response = feedBackApi.sendFeedBackApi(info)
            if (response.isSuccessful) {
                response.body()?.body?.value?.let { msg ->
                    ApisResponse.Success(msg)
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err_emoji, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(e, null)
        }
        emit(data)
    }.flowOn(Dispatchers.IO)


    fun getFeedBackInvoiceResponse(info: FinalInvoiceSendRequest) = flow {
        emit(ApisResponse.Loading("Sending Invoice FeedBack.."))
        val data = try {
            val response = feedBackApi.sendFeedBackInvoiceApi(info)
            if (response.isSuccessful) {
                response.body()?.body?.let { msg ->
                    ApisResponse.Success(msg)
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err_emoji, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(e, null)
        }
        emit(data)
    }.flowOn(Dispatchers.IO)


}