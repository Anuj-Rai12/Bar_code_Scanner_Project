package com.fbts.mpos.ui.oderconfirm.repo.confirmorder

import android.util.Log
import com.fbts.mpos.api.confirmOrder.ConfirmOrderApi
import com.fbts.mpos.data.confirmOrder.ConfirmOrderRequest
import com.fbts.mpos.utils.ApisResponse
import com.fbts.mpos.utils.buildApi
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
            val info = if (response.isSuccessful) {
                Log.i("saveUserOrderItem", response.message())
                response.body()
            } else {
                Log.i("saveUserOrderItem", response.message())
                null
            }
            ApisResponse.Success(info)
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(res)
    }


}