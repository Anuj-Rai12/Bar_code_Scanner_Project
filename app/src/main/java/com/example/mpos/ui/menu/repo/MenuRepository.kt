package com.example.mpos.ui.menu.repo

import com.example.mpos.api.menu.MenuApi
import com.example.mpos.data.mnu.request.MenuItemRequest
import com.example.mpos.data.mnu.response.json.MenuDataResponse
import com.example.mpos.utils.ApisResponse
import com.example.mpos.utils.buildApi
import com.example.mpos.utils.deserializeFromJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit

class MenuRepository constructor(retrofit: Retrofit) {
    private val api = buildApi<MenuApi>(retrofit)


    companion object {
        const val err = "Oops Something Went Wrong!!"
        const val nullError = "Cannot load Response \n Please Try Again!!"
    }

    fun getMenuData(request: MenuItemRequest) = flow {
        emit(ApisResponse.Loading("Loading Menu"))
        val data = try {
            val response = api.getMenuResponse(request)
            if (response.isSuccessful) {
                deserializeFromJson<MenuDataResponse>(response.body()?.categoryMenuResult?.value)?.let {
                    ApisResponse.Success(it)
                } ?: ApisResponse.Error(nullError, null)
            } else {
                ApisResponse.Error(err, null)
            }
        } catch (e: Exception) {
            ApisResponse.Error(null, e)
        }
        emit(data)
    }.flowOn(IO)

}