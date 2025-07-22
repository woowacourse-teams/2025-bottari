package com.bottari.data.util

import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

@Suppress("UNCHECKED_CAST")
inline fun <T> safeApiCall(apiCall: () -> Response<T>): Result<T> =
    try {
        val response = apiCall()

        if (response.isSuccessful) {
            Result.success(response.body() as T)
        } else {
            Result.failure(HttpException(response))
        }
    } catch (exception: Exception) {
        Timber.e(exception, exception.message)
        Result.failure(exception)
    }
