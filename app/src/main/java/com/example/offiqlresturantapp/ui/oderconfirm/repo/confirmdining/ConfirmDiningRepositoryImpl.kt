package com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmdining

import android.util.Log
import com.example.offiqlresturantapp.api.confirmDining.ConfirmDiningApi
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.buildApi
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