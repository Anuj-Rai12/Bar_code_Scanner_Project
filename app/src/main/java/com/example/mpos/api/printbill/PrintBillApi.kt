package com.example.mpos.api.printbill

import com.example.mpos.data.printEstKot.request.PrintEstKotRequest
import com.example.mpos.data.printEstKot.response.PrintEstKotResponse
import com.example.mpos.data.printbIll.PrintBillRequest
import com.example.mpos.data.printbIll.PrintBillResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PrintBillApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.printBill}")
    @POST(AllStringConst.End_point)
    suspend fun getPrintBillResponse(@Body request: PrintBillRequest): Response<PrintBillResponse>

    @Headers("${AllStringConst.SoapAction.HeaderKey} ${AllStringConst.SoapAction.printEstPos}")
    @POST(AllStringConst.End_point)
    suspend fun printKotEst(
        @Body request: PrintEstKotRequest
    ): Response<PrintEstKotResponse>

}