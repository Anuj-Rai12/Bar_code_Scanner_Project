package com.example.mpos.ui.oderconfirm.repo.printbill

import com.example.mpos.api.printbill.PrintBillApi
import com.example.mpos.data.printEstKot.request.PrintEstKotRequest
import com.example.mpos.data.printEstKot.response.json.PrintJsonKotResponse
import com.example.mpos.data.printbIll.PrintBillRequest
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class PrintBillRepository(retrofit: Retrofit) {

    private val api = buildApi<PrintBillApi>(retrofit)

    private val noDataFound = "Cannot Find Response for it!!"

    private val error = "Oops SomeThing Went Wong!!"

    fun getPrintBillResponse(request: PrintBillRequest) = flow {
        emit(ApisResponse.Loading("Please wait"))
        val data = try {
            val response = api.getPrintBillResponse(request)
            if (response.isSuccessful) {
                response.body()?.responseForBody?.value?.let {
                    ApisResponse.Success(it)
                } ?: ApisResponse.Error(noDataFound, null)
            } else {
                ApisResponse.Error(error, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


    fun printKotResponse(request: PrintEstKotRequest) = flow {
        emit(ApisResponse.Loading("Please wait"))
        val data = try {
            val response = api.printKotEst(request)
            if (response.isSuccessful) {
                deserializeFromJson<PrintJsonKotResponse>(response.body()?.responseForBody?.value)?.let {
                    ApisResponse.Success(it)
                } ?: ApisResponse.Error(noDataFound, null)
            } else {
                ApisResponse.Error(error, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }
}