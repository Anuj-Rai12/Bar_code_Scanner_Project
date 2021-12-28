package com.example.offiqlresturantapp.di

import com.example.offiqlresturantapp.api.ApiInterface
import com.example.offiqlresturantapp.utils.ApiPostResponseObj
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {
    /*hashMap["Authorization"] = ApiPostResponseObj.authHeader
    hashMap["SOAPAction"] = "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:APKlogin"
    hashMap["Content-Type"] = "application/xml"
    addInterceptor(Interceptor {
            val operation = it.request().newBuilder()
                .addHeader("Authorization", ApiPostResponseObj.authHeader)
                .addHeader(
                    "SOAPAction",
                    "urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:APKlogin")
                .addHeader("Content-Type", "application/xml")
                .build()
            it.proceed(operation)
        })
    */
    private val client = OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/")
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    //http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/LoginAndGetMasterAPI
    @Singleton
    @Provides
    fun api(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }
}