package com.example.mpos.ui.oderconfirm.repo.confirmorder

import android.util.Log
import com.example.mpos.api.confirmOrder.ConfirmOrderApi
import com.example.mpos.data.confirmOrder.ConfirmOrderRequest
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit

class ConfirmOrderRepositoryImpl(
    retrofit: Retrofit
) : ConfirmOrderRepository {

    val api = buildApi<ConfirmOrderApi>(retrofit)


    override fun saveUserOrderItem(confirmOrderRequest: ConfirmOrderRequest) = flow {
        emit(ApisResponse.Loading("Adding Order.."))
        val res = try {
            val response = api.sendPostRequestApi(confirmOrderRequest)
            if (response.isSuccessful) {
                if (confirmOrderRequest.body!!.getPrintBody.toBoolean()) {//Get Receipt
                    val status = response.body()?.body?.returnValue
                    Log.i("TESTING_JSON", "saveUserOrderItem: $status")
                    if (status == "01" || status.isNullOrEmpty()) {
                        ApisResponse.Error("Order is Not Inserted in Navision at All.", null)
                    } else {
                        ApisResponse.Success(deserializeFromJson<PrintReceiptInfo>(status))
                    }
                } else { // Get Status
                    val status = response.body()?.body?.returnValue
                    if (status == "01" || status.isNullOrEmpty()) {
                        ApisResponse.Error("Order is Not Inserted in Navision at All.", null)
                    } else {
                        ApisResponse.Success("Order is Inserted in Navision at All.")
                    }
                }
            } else {
                ApisResponse.Error(
                    "Oops Something went wrong no able to add order the The Table",
                    null
                )
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(res)
    }

}