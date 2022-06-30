package com.example.offiqlresturantapp.ui.oderconfirm.repo.posline

import com.example.offiqlresturantapp.api.poslineapi.PosLineApi
import com.example.offiqlresturantapp.data.poslineitem.request.PosLineItemApiRequest
import com.example.offiqlresturantapp.utils.ApisResponse
import com.example.offiqlresturantapp.utils.buildApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class PosLineRepository(retrofit: Retrofit) {

    val api = buildApi<PosLineApi>(retrofit)

    fun getPosLineResponse(body: PosLineItemApiRequest) = flow {
        emit(ApisResponse.Loading("Submitting Items.."))
        val data = try {
            val response = api.sendPostRequest(body)
            val apiRes = if (response.isSuccessful) {
                response.body()?.let {
                    val res = it.responseForBody?.value ?: ""
                    return@let if (!res.startsWith("01") && res.isNotEmpty()) {
                        ApisResponse.Success("Success fully Inserted...")
                    } else {
                        ApisResponse.Error(
                            "Oops!! Looks Like Cannot Insert The Menu Item \nTry Again?",
                            null
                        )
                    }
                } ?: ApisResponse.Error("Cannot get the Response", null)
            } else {
                ApisResponse.Error("Oops Something Went Wrong\nCannot Upload Menu Items", null)
            }
            apiRes
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)


}