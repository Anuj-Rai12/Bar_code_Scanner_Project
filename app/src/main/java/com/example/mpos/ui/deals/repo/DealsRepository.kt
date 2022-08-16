package com.example.mpos.ui.deals.repo

import com.example.mpos.api.deal.DealsApi
import com.example.mpos.data.deals.DealsRequest
import com.example.mpos.data.deals.json.DealsJsonResponse
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import com.example.mpos.utils.getEmojiByUnicode
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class DealsRepository(retrofit: Retrofit) {

    private val api = buildApi<DealsApi>(retrofit)

    private val err = "Oops something went wrong cannot load Request"

    fun getDealsResponse(requestBody: DealsRequest) = flow {
        emit(ApisResponse.Loading("Find Deals of the Day ${getEmojiByUnicode(0x1F372)}"))
        val data = try {
            val response = api.getDeals(requestBody)
            if (response.isSuccessful) {
                val str = response.body()?.body?.value ?: err
                val jsonRes = getJsonOrStringResponse<DealsJsonResponse>(str)
                if (jsonRes is String) {
                    ApisResponse.Error(jsonRes, null)
                } else {
                    (jsonRes as DealsJsonResponse?)?.let {
                        ApisResponse.Success(it)
                    } ?: ApisResponse.Error(err, null)
                }
            } else {
                ApisResponse.Error("Cannot find the Deals", null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(e, null)
        }
        emit(data)
    }.flowOn(IO)

    companion object {
        inline fun <reified T> getJsonOrStringResponse(json: String): Any? {
            return try {
                deserializeFromJson<T>(json)
            } catch (e: JsonSyntaxException) {
                json
            }
        }
    }
}