package com.example.offiqlresturantapp.api



import com.example.offiqlresturantapp.model.item.BodyPostItem
import com.example.offiqlresturantapp.model.item.EnvelopeItemPost
import com.example.offiqlresturantapp.model.item.ItemEnvelope
import com.example.offiqlresturantapp.model.item.ItemMasterSyncPost
import com.example.offiqlresturantapp.model.test.apklogin.EnvelopePostItem
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("LoginAndGetMasterAPI")
    suspend fun sendApiPostRequest(
        @HeaderMap hashMap: HashMap<String, String>,
        @Body request: EnvelopePostItem
    ): Response<ResponseBody>

    @POST("LoginAndGetMasterAPI")
    suspend fun getApiPostRequest(
        @HeaderMap hashMap: HashMap<String, String>,
        @Body post: EnvelopeItemPost = EnvelopeItemPost(body = BodyPostItem(ItemMasterSyncPost()))
    ): Response<ItemEnvelope>

    //@Body post: EnvelopeItemPost = EnvelopeItemPost(body = BodyPostItem(itemMasterSync = ItemMasterSyncPost()))
}