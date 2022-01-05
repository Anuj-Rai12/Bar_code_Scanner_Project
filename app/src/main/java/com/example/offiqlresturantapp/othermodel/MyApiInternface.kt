package com.example.offiqlresturantapp.othermodel

import retrofit2.Response
import retrofit2.http.GET

interface MyApiInterface {

    @GET("/gateway")
    suspend fun sendApiInterface(): Response<ResponseOp>

}