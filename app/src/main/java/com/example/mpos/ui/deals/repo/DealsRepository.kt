package com.example.mpos.ui.deals.repo

import com.example.mpos.api.deal.DealsApi
import com.example.mpos.data.deals.DealsRequest
import com.example.mpos.data.deals.confirmdeals.ConfirmDealsRequest
import com.example.mpos.data.deals.json.DealsJsonResponse
import com.example.mpos.data.deals.scan_and_find_deals.ScanAndFindDealsRequest
import com.example.mpos.data.deals.scan_and_find_deals.json.ScanAndFindDealsJson
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

    fun getDealsResponse(requestBody: DealsRequest) = flow {
        emit(ApisResponse.Loading("Find Deals of the Day ${getEmojiByUnicode(0x1F372)}"))
        val data = try {
            val response = api.getDeals(requestBody)
            if (response.isSuccessful) {
                val str = response.body()?.body?.value ?: err
                getJsonOrStringResponse<DealsJsonResponse>(str)
            } else {
                ApisResponse.Error("Cannot find the Deals", null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(e, null)
        }
        emit(data)
    }.flowOn(IO)


    fun postDealsResponse(requestBody: ConfirmDealsRequest) = flow {
        emit(ApisResponse.Loading("Adding Deals..."))
        val data=try {
            val response = api.postDealsToApi(requestBody)
            if (response.isSuccessful) {
                if (response.body()?.body?.value.isNullOrEmpty()) {
                    ApisResponse.Error("Cannot get a Response ", null)
                } else {
                    val value = response.body()?.body?.value!!
                    if (value.startsWith("01")) {
                        ApisResponse.Success(requestBody.body?.rcptNo!!)
                    } else {
                        ApisResponse.Error(value, null)
                    }
                }
            } else {
                ApisResponse.Error("Cannot Add Deals Response", null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(e, null)
        }
        emit(data)
    }.flowOn(IO)


    fun getScanDealsApiResponse(request: ScanAndFindDealsRequest) = flow {
        emit(ApisResponse.Loading("Loading deals Item"))
        val data = try {
            val response = api.getDealsItem(request)
            if (response.isSuccessful) {
                getJsonOrStringResponse<ScanAndFindDealsJson>(response.body()?.body?.value!!)
            } else {
                ApisResponse.Error("Failed to load Combo Item", null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(e, null)
        }
        emit(data)
    }.flowOn(IO)


    companion object {
        const val err = "Oops something went wrong cannot load Request"
        inline fun <reified T> getJsonOrStringResponse(
            json: String,
            error: String = err
        ): ApisResponse<out Any> {
            val jsonRes = try {
                deserializeFromJson<T>(json)
            } catch (e: JsonSyntaxException) {
                json
            }
            return if (jsonRes is String) {
                ApisResponse.Error(jsonRes, null)
            } else {
                jsonRes?.let {
                    ApisResponse.Success(it)
                } ?: ApisResponse.Error(error, null)
            }
        }
    }
}