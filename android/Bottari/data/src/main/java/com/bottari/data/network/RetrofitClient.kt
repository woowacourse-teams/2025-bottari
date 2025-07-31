package com.bottari.data.network

import com.bottari.data.BuildConfig
import com.bottari.data.common.util.PrettyJsonLogger
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object RetrofitClient {
    private val contentType = "application/json".toMediaType()
    private val json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient
            .Builder()
            .addInterceptor(provideLoggingInterceptor())
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
    }

    inline fun <reified T> create(): T = retrofit.create(T::class.java)

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(PrettyJsonLogger()).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}
