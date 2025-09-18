package com.bottari.domain.model.exception

sealed interface BottariResult<T> {
    data class Success<T>(
        val data: T,
    ) : BottariResult<T>

    data class ApiException<T>(
        val exception: BottariException,
    ) : BottariResult<T>

    data class ApiError<T>(
        val throwable: Throwable = Throwable("Unknown Error"),
    ) : BottariResult<T>
}

inline fun <T> BottariResult<T>.onSuccess(action: (value: T) -> Unit): BottariResult<T> {
    if (this is BottariResult.Success) action(data)
    return this
}

inline fun <T> BottariResult<T>.onApiException(action: (BottariException) -> Unit): BottariResult<T> {
    if (this is BottariResult.ApiException) action(exception)
    return this
}

inline fun <T> BottariResult<T>.onApiError(action: (Throwable) -> Unit): BottariResult<T> {
    if (this is BottariResult.ApiError) action(throwable)
    return this
}

inline fun <T, R> BottariResult<T>.map(transform: (T) -> R): BottariResult<R> =
    when (this) {
        is BottariResult.Success -> BottariResult.Success(transform(data))
        is BottariResult.ApiException -> BottariResult.ApiException(exception)
        is BottariResult.ApiError -> BottariResult.ApiError(throwable)
    }

inline fun <T, R> BottariResult<T>.mapCatching(transform: (T) -> R): BottariResult<R> =
    when (this) {
        is BottariResult.Success -> {
            runCatching {
                BottariResult.Success(transform(data))
            }.getOrElse { throwable ->
                when (throwable) {
                    is BottariException -> BottariResult.ApiException(throwable)
                    else -> BottariResult.ApiError(throwable)
                }
            }
        }

        is BottariResult.ApiException -> BottariResult.ApiException(exception)
        is BottariResult.ApiError -> BottariResult.ApiError(throwable)
    }

fun <T> Result<T>.toBottariResult(): BottariResult<T> =
    fold(
        onSuccess = { BottariResult.Success(it) },
        onFailure = { throwable ->
            when (throwable) {
                is BottariException -> BottariResult.ApiException(throwable)
                else -> BottariResult.ApiError(throwable)
            }
        },
    )

fun <T> Result<BottariResult<T>>.getOrConvert(): BottariResult<T> =
    getOrElse { throwable ->
        when (throwable) {
            is BottariException -> BottariResult.ApiException(throwable)
            else -> BottariResult.ApiError(throwable)
        }
    }

fun <T> BottariResult<T>.getOrNull(): T? =
    when (this) {
        is BottariResult.Success -> data
        is BottariResult.ApiException,
        is BottariResult.ApiError,
        -> null
    }

fun <T> BottariResult<T>.getOrThrow(): T =
    when (this) {
        is BottariResult.Success -> data
        is BottariResult.ApiException -> throw exception
        is BottariResult.ApiError -> throw throwable
    }
