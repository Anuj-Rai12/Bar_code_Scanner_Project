package com.example.mpos.ui.cost.repo

import com.example.mpos.api.billing.BillingApi
import com.example.mpos.api.confirmDining.ConfirmDiningApi
import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingRequest
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcRequest
import com.example.mpos.data.checkBillingStatus.CheckBillingStatusRequest
import com.example.mpos.data.costestimation.request.CostEstimation
import com.example.mpos.ui.menu.repo.MenuRepository
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class CostDashBoardRepository(retrofit: Retrofit) {

    private val api = buildApi<ConfirmDiningApi>(retrofit)

    private val billingApi = buildApi<BillingApi>(retrofit)

    fun getCostEstimationParams(request: CostEstimation) = flow {
        emit(ApisResponse.Loading("Uploading the Item.."))
        val data = try {
            val response = api.setPostCostEstimationApi(request = request)
            if (response.isSuccessful) {
                response.body()?.let { estimation ->
                    if (!estimation.body?.errorFound.toBoolean() && estimation.body?.returnValue.toBoolean()) {
                        ApisResponse.Success(null)
                    } else {
                        ApisResponse.Error(
                            estimation.body?.errorText ?: "Cannot Upload the menu item", null
                        )
                    }
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun confirmBilling(request: ConfirmBillingRequest) = flow {
        emit(ApisResponse.Loading("validating Billing Request"))
        val data = try {
            val response = billingApi.postConfirmBillingApi(request = request)
            if (response.isSuccessful) {
                response.body()?.let { estimation ->
                    if (!estimation.body?.errorFound.toBoolean() && estimation.body?.returnValue.toBoolean()) {
                        ApisResponse.Success("${request.body?.rcptNo}")
                    } else {
                        ApisResponse.Error(
                            estimation.body?.errorText ?: "Cannot Upload the menu item", null
                        )
                    }
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun sendBillToEdc(request: ScanBillingToEdcRequest) = flow {
        emit(ApisResponse.Loading("Sending Bill To EDC.."))
        val data = try {
            val response = billingApi.postSendBillingApi(request = request)
            if (response.isSuccessful) {
                response.body()?.let { estimation ->
                    if (!estimation.body?.errorFound.toBoolean() && estimation.body?.returnValue.toBoolean()) {
                        ApisResponse.Success(null)
                    } else {
                        ApisResponse.Error(
                            estimation.body?.errorText ?: "Cannot Upload the menu item", null
                        )
                    }
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun checkBillStatus(request: CheckBillingStatusRequest) = flow {
        val data = try {
            val response = billingApi.checkBillStatusApi(request = request)
            if (response.isSuccessful) {
                response.body()?.let { res ->
                    if (res.body?.returnValue.toBoolean()){
                        ApisResponse.Success(null)
                    }else{
                        ApisResponse.Error("Cannot Find status for Receipt ${request.body?.mPosDoc}",null)
                    }
                } ?: ApisResponse.Error(MenuRepository.nullError, null)
            } else {
                ApisResponse.Error(MenuRepository.err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}