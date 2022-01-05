package com.example.offiqlresturantapp.othermodel

import retrofit2.Response
import retrofit2.http.GET

interface RssApiInterface {

    @GET("/rss/books.xml")
    suspend fun getRssFeed():Response<RssOp>
}