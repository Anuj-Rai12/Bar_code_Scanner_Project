package com.example.offiqlresturantapp.ui.oderconfirm.repo.confirmdining

import android.util.Log
import com.example.offiqlresturantapp.api.confirmDining.ConfirmDiningApi
import com.example.offiqlresturantapp.data.cofirmDining.ConfirmDiningRequest
import com.example.offiqlresturantapp.di.NewRetrofitInstance
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.buildApi
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import javax.inject.Inject

class ConfirmDiningRepositoryImpl @Inject constructor(
    @NewRetrofitInstance retrofit: Retrofit
) : ConfirmDiningRepository {
    val api = buildApi<ConfirmDiningApi>(retrofit)


    override fun updateAndLockTbl(confirmDiningRequest: ConfirmDiningRequest) = flow {
        emit(ApisResponse.Loading("Please Updating the Information"))
        val res = try {
            val res = api.setPostRequestApi(confirmDiningRequest)
            if (res.isSuccessful) {
                ApisResponse.Success(res.body())
            } else {
                Log.i("updateAndLockTbl", " ${res.message()}")
                ApisResponse.Success(null)
            }
        } catch (e: Exception) {
            Log.i("updateAndLockTbl", e.localizedMessage.toString())
            ApisResponse.Error(null, e)
        }
        emit(res)
    }


}