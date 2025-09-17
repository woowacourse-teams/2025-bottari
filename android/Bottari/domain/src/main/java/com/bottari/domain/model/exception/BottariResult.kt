package com.bottari.domain.model.exception

sealed interface BottariResult<T> {
    data class Success<T>(
        val data: T,
    ) : BottariResult<T>

    data class Created<T>(
        val createdId: Long,
    ) : BottariResult<T>

    data class ApiError<T>(
        val exception: BottariException,
    ) : BottariResult<T>

    data class NetworkError<T>(
        val throwable: Throwable,
    ) : BottariResult<T>
}

inline fun <T> BottariResult<T>.onSuccess(action: (value: T) -> Unit): BottariResult<T> {
    if (this is BottariResult.Success) action(data)
    return this
}

inline fun <T> BottariResult<T>.onApiError(action: (BottariException) -> Unit): BottariResult<T> {
    if (this is BottariResult.ApiError) action(exception)
    return this
}

inline fun <T> BottariResult<T>.onNetworkError(action: (Throwable) -> Unit): BottariResult<T> {
    if (this is BottariResult.NetworkError) action(throwable)
    return this
}
