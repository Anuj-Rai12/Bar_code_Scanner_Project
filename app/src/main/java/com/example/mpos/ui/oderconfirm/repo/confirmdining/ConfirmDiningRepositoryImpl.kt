package com.example.mpos.ui.oderconfirm.repo.confirmdining

import android.util.Log
import com.example.mpos.api.confirmDining.ConfirmDiningApi
import com.example.mpos.data.cofirmDining.ConfirmDiningRequest
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit

class ConfirmDiningRepositoryImpl(
    retrofit: Retrofit
) : ConfirmDiningRepository {
    val api = buildApi<ConfirmDiningApi>(retrofit)


    override fun updateAndLockTbl(confirmDiningRequest: ConfirmDiningRequest) = flow {
        emit(ApisResponse.Loading("Updating Table.."))
        val res = try {
            val res = api.setPostRequestApi(confirmDiningRequest)
            if (res.isSuccessful) {
                ApisResponse.Success(res.body())
            } else {
                Log.i("updateAndLockTbl", " ${res.message()}")
                ApisResponse.Success(null)
            }
        } catch (e: Exception) {
            Log.i("updateAndLockTbl", " ${e.localizedMessage}")
            ApisResponse.Error(null, e)
        }
        emit(res)
    }


}