package com.bottari.data.network

import com.bottari.data.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitClient(
    private val okHttpClient: OkHttpClient,
) {
    private val contentType = "application/json".toMediaType()
    private val json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
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
}
