package com.example.offiqlresturantapp.di

import com.example.offiqlresturantapp.api.ApiInterface
import com.example.offiqlresturantapp.othermodel.MyApiInterface
import com.example.offiqlresturantapp.othermodel.RssApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*


@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Singleton
    @Provides
    fun createInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
        // Add Interceptor to HttpClient

        // Add Interceptor to HttpClient
        //val client: OkHttpClient = Builder().addInterceptor(interceptor).build()
    }
    //var client: OkHttpClient = Ok.addInterceptor(interceptor).build()
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
/*
    @Singleton
    @Provides
    fun getUnsafeOkHttpClient(): OkHttpClient? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    //@SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    //@SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
*//*
                val acceptedIssuers: Array<X509Certificate?>?
                    get() = arrayOf()*//*
                }
            )

            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val trustManager =
                trustAllCerts[0] as X509TrustManager
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }*/

    /*fun getUnsafeOkHttpClient(): OkHttpClient? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> =
                trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }

            val trustManager =
                trustManagers[0] as X509TrustManager


            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustManager)
            builder.connectTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }*/


    @Singleton
    @Provides
    fun client(interceptor: HttpLoggingInterceptor)= OkHttpClient.Builder().apply {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
    }.build()

    @Singleton
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    //First Demo 1 -> https://demo.autodns.com/
//http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/
    //http://223.31.53.229:28360/NAVUSER/WS/HLDGRP/Codeunit/LoginAndGetMasterAPI
    @Singleton
    @Provides
    fun api(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun api2(retrofit: Retrofit): MyApiInterface {
        return retrofit.create(MyApiInterface::class.java)
    }


    @Singleton
    @Provides
    fun api3(retrofit: Retrofit): RssApiInterface {
        return retrofit.create(RssApiInterface::class.java)
    }

}