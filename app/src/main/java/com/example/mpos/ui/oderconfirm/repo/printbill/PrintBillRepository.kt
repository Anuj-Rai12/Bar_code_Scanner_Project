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
            //val response = api.printKotEst(request)
            deserializeFromJson<PrintJsonKotResponse>("{\"HeaderTxt\": \"KOT\",\"HeaderTxt1\": \"Cronus restaurant\",\"HeaderTxt2\": \"CkLifeStyle Pvt. Ltd.\",\"HeaderTxt3\": \"Tel : 011 40194902\",\"HeaderTxt4\": \"Website :- www.CronusIndia.in\",\"HeaderTxt5\": \"Shop No. S8 &amp; S9 Pacific Mall, \",\"HeaderTxt6\": \"Tagore Garden, Najafgarh Road\",\"HeaderTxt7\": \"New Delhi-110018\",\"BillTypeTxt\": \"\",\"BillType\": \"RESTAURANT\",\"SubHeaderTxt1\": \"Estimate No.:PC012000000475\",\"SubHeaderTxt2\": \"DateTime:11/03/23/ 1:49:44 PM\",\"SubHeaderTxt3\": \"Table:42\",\"SubHeaderTxt4\": \"Trans.:0\",\"SubHeaderTxt5\": \"Staff:0000\",\"childitemList\": [],\"BaseAmount\": \"0\"}")?.let {
                ApisResponse.Success(it)
            } ?: ApisResponse.Error(noDataFound, null)
          /*  if (response.isSuccessful) {
                deserializeFromJson<PrintJsonKotResponse>(response.body()?.responseForBody?.value)?.let {
                    ApisResponse.Success(it)
                } ?: ApisResponse.Error(noDataFound, null)
            } else {
                ApisResponse.Error(error, null)
            }*/
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }
}