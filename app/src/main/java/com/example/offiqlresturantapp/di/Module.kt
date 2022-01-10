package com.example.offiqlresturantapp.di

import com.example.offiqlresturantapp.utils.AllStringConst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    //.addHeader("SOAPAction","urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:APKlogin")

    private val httpInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val client = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor {
                val operation = it.request().newBuilder()
                    .addHeader("Authorization", AllStringConst.authHeader)
                    .addHeader("Content-Type", "application/xml")
                    .build()
                it.proceed(operation)
            }
            .addInterceptor(httpInterceptor)
    }.build()


    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(client)
            .baseUrl(AllStringConst.BASE_URL)
            .build()
    }
}