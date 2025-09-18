package com.bottari.domain.model.exception

sealed interface BottariResult<T> {
    data class Success<T>(
        val data: T,
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

inline fun <T, R> BottariResult<T>.map(transform: (T) -> R): BottariResult<R> =
    when (this) {
        is BottariResult.Success -> BottariResult.Success(transform(data))
        is BottariResult.ApiError -> BottariResult.ApiError(exception)
        is BottariResult.NetworkError -> BottariResult.NetworkError(throwable)
    }

inline fun <T, R> BottariResult<T>.mapCatching(transform: (T) -> R): BottariResult<R> =
    when (this) {
        is BottariResult.Success -> {
            runCatching {
                BottariResult.Success(transform(data))
            }.getOrElse { throwable ->
                when (throwable) {
                    is BottariException -> BottariResult.ApiError(throwable)
                    else -> BottariResult.NetworkError(throwable)
                }
            }
        }

        is BottariResult.ApiError -> BottariResult.ApiError(exception)
        is BottariResult.NetworkError -> BottariResult.NetworkError(throwable)
    }

fun <T> Result<T>.toBottariResult(): BottariResult<T> =
    fold(
        onSuccess = { BottariResult.Success(it) },
        onFailure = { throwable ->
            when (throwable) {
                is BottariException -> BottariResult.ApiError(throwable)
                else -> BottariResult.NetworkError(throwable)
            }
        },
    )

fun <T> Result<BottariResult<T>>.getOrConvert(): BottariResult<T> =
    getOrElse { throwable ->
        when (throwable) {
            is BottariException -> BottariResult.ApiError(throwable)
            else -> BottariResult.NetworkError(throwable)
        }
    }

fun <T> BottariResult<T>.getOrNull(): T? =
    when (this) {
        is BottariResult.Success -> data
        is BottariResult.ApiError,
        is BottariResult.NetworkError,
        -> null
    }

fun <T> BottariResult<T>.getOrThrow(): T =
    when (this) {
        is BottariResult.Success -> data
        is BottariResult.ApiError -> throw exception
        is BottariResult.NetworkError -> throw throwable
    }
