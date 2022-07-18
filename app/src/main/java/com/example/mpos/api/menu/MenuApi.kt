package com.example.mpos.api.menu

import com.example.mpos.data.mnu.request.MenuItemRequest
import com.example.mpos.data.mnu.response.MenuItemResponse
import com.example.mpos.utils.AllStringConst
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MenuApi {

    @Headers("${AllStringConst.SoapAction.HeaderKey}${AllStringConst.SoapAction.categoryMenu}")
    @POST(AllStringConst.End_point)
    suspend fun getMenuResponse(@Body request: MenuItemRequest): Response<MenuItemResponse>
}