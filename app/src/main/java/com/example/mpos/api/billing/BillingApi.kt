package com.example.mpos.api.billing

import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingRequest
import com.example.mpos.data.billing.conifrm_billing.ConfirmBillingResponse
import com.example.mpos.data.billing.printInvoice.request.PrintInvoiceRequest
import com.example.mpos.data.billing.printInvoice.response.PrintInvoiceResponse
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcRequest
import com.example.mpos.data.billing.send_billing_to_edc.ScanBillingToEdcResponse
import com.example.mpos.data.checkBillingStatus.CheckBillingStatusRequest
import com.example.mpos.data.checkBillingStatus.CheckBillingStatusResponse
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

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.sendBillingEtc}")
    @POST(AllStringConst.End_point)
    suspend fun postSendBillingApi(
        @Body request: ScanBillingToEdcRequest
    ): Response<ScanBillingToEdcResponse>


    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.checkStatusBillingEDC}")
    @POST(AllStringConst.End_point)
    suspend fun checkBillStatusApi(
        @Body request: CheckBillingStatusRequest
    ): Response<CheckBillingStatusResponse>


    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.printBillInvoice}")
    @POST(AllStringConst.End_point)
    suspend fun getPrintBillInvoiceResponse(@Body request: PrintInvoiceRequest): Response<PrintInvoiceResponse>

}