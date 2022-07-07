package com.fbts.mpos.ui.oderconfirm.repo.confirmdining

import android.util.Log
import com.fbts.mpos.api.confirmDining.ConfirmDiningApi
import com.fbts.mpos.data.cofirmDining.ConfirmDiningRequest
import com.fbts.mpos.utils.ApisResponse
import com.fbts.mpos.utils.buildApi
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