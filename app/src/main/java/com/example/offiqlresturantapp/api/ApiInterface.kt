package com.example.offiqlresturantapp.api


import com.example.offiqlresturantapp.model.item.BodyPostItem
import com.example.offiqlresturantapp.model.item.EnvelopeItemPost
import com.example.offiqlresturantapp.model.item.ItemEnvelope
import com.example.offiqlresturantapp.model.item.ItemMasterSyncPost
import com.example.offiqlresturantapp.model.test.EnvelopeItemSync
import com.example.offiqlresturantapp.model.test.EnvelopeItemSyncResponse
import com.example.offiqlresturantapp.model.test.apkJanLogin.EnvelopeOption
import com.example.offiqlresturantapp.model.test.apkJanLogin.EnvelopePostApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface ApiInterface {

    @POST("LoginAndGetMasterAPI")
    suspend fun sendApiPostRequest(
        @HeaderMap hashMap: HashMap<String, String>,
        @Body request: EnvelopeOption
    ): Response<EnvelopePostApiResponse>

    @POST("LoginAndGetMasterAPI")
    suspend fun getApiPostRequest(
        @HeaderMap hashMap: HashMap<String, String>,
        @Body request: EnvelopeItemSync//post: EnvelopeItemPost = EnvelopeItemPost(body = BodyPostItem(ItemMasterSyncPost()))
    ): Response<EnvelopeItemSyncResponse>

}