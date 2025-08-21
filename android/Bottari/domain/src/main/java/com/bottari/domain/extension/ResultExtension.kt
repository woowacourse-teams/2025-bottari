package com.bottari.domain.extension

/**
 * 성공 값만 변환합니다.
 *
 * 변환 과정에서 예외가 발생하면 그대로 던집니다.
 */
inline fun <T, R> Result<T>.map(transform: (T) -> R): Result<R> =
    fold(
        onSuccess = { Result.success(transform(it)) },
        onFailure = { Result.failure(it) },
    )

/**
 * 성공 값만 변환합니다.
 *
 * 변환 과정에서 예외가 발생하면 [Result.failure] 로 감쌉니다.
 */
inline fun <T, R> Result<T>.mapCatching(transform: (T) -> R): Result<R> =
    fold(
        onSuccess = { runCatching { transform(it) } },
        onFailure = { Result.failure(it) },
    )

/**
 * 성공 값을 [Result] 로 변환합니다.
 *
 * 변환 과정에서 예외가 발생하면 그대로 던집니다.
 */
inline fun <T, R> Result<T>.flatMap(transform: (T) -> Result<R>): Result<R> =
    fold(
        onSuccess = { transform(it) },
        onFailure = { Result.failure(it) },
    )

/**
 * 성공 값을 [Result] 로 변환합니다.
 *
 * 변환 과정에서 예외가 발생하면 [Result.failure] 로 감쌉니다.
 */
inline fun <T, R> Result<T>.flatMapCatching(transform: (T) -> Result<R>): Result<R> =
    fold(
        onSuccess = {
            runCatching { transform(it) }.getOrElse { throwable -> Result.failure(throwable) }
        },
        onFailure = { Result.failure(it) },
    )
