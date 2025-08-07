package com.bottari.data.common.util

import com.bottari.logger.BottariLogger
import retrofit2.HttpException
import retrofit2.Response

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
        BottariLogger.error(exception.message, exception)
        Result.failure(exception)
    }
