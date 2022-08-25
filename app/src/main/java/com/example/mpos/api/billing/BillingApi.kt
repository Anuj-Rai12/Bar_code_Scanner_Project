package com.example.mpos.api.billing

import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingRequest
import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingResponse
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcRequest
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BillingApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.confirmBilling}")
    @POST(AllStringConst.End_point)
    suspend fun postConfirmBillingApi(
        @Body request: ConfirmBillingRequest
    ): Response<ConfirmBillingResponse>

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.confirmBilling}")
    @POST(AllStringConst.End_point)
    suspend fun postSendBillingApi(
        @Body request: ScanBillingToEdcRequest
    ): Response<ScanBillingToEdcResponse>

}