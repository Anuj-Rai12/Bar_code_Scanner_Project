package com.example.mpos.ui.oderconfirm.repo.posline

import com.example.mpos.api.poslineapi.PosLineApi
import com.example.mpos.data.poslineitem.request.PosLineItemApiRequest
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
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
                    return@let if (res.isNotEmpty() && !res.startsWith("01")) {
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