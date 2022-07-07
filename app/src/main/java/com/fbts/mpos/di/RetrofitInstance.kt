package com.fbts.mpos.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInstance(
    private val auth: String,
    private val baseUrl: String
) {

    private val httpInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    companion object {

        @Volatile
        private var INSTANCE: RetrofitInstance? = null

        fun getInstance(auth: String, baseUrl: String): RetrofitInstance {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = RetrofitInstance(auth, baseUrl)
                }
                return INSTANCE!!
            }
        }
    }


    private val client = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor {
                val operation = it.request().newBuilder()
                    .addHeader("Authorization", auth)
                    .addHeader("Content-Type", "application/xml")
                    .build()
                it.proceed(operation)
            }
            .addInterceptor(httpInterceptor)
    }.build()


    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(client)
            .baseUrl(baseUrl)
            .build()
    }

}