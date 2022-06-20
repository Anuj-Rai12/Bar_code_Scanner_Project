package com.example.offiqlresturantapp.di

import android.app.Application
import androidx.room.Room
import com.example.offiqlresturantapp.db.RoomDataBaseInstance
import com.example.offiqlresturantapp.utils.AllStringConst
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    //.addHeader("SOAPAction","urn:microsoft-dynamics-schemas/codeunit/LoginAndGetMasterAPI:Login")

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


    @NewRetrofitInstance
    @Singleton
    @Provides
    fun getRetrofit2(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(client)
            .baseUrl(AllStringConst.BASE_URL_2)
            .build()
    }


    @Provides
    @Singleton
    fun getDataBaseInstance(app: Application) = Room.databaseBuilder(
        app,
        RoomDataBaseInstance::class.java,
        RoomDataBaseInstance.DatabaseName
    ).fallbackToDestructiveMigration().build()


    @Singleton
    @Provides
    fun providesCoroutines() = CoroutineScope(SupervisorJob())
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewRetrofitInstance