package com.bottari.data.network.adapter

import com.bottari.data.model.remote.common.ApiErrorCode
import com.bottari.data.model.remote.common.ErrorResponse
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.BottariResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class BottariCall<R>(
    private val delegate: Call<R>,
    private val successType: Type,
) : Call<BottariResult<R>> {
    override fun enqueue(callback: Callback<BottariResult<R>>) {
        delegate.enqueue(
            object : Callback<R> {
                override fun onResponse(
                    call: Call<R>,
                    response: Response<R>,
                ) = callback.onResponse(
                    this@BottariCall,
                    Response.success(response.toBottariResult()),
                )

                override fun onFailure(
                    call: Call<R>,
                    t: Throwable,
                ) = callback.onResponse(
                    this@BottariCall,
                    Response.success(BottariResult.NetworkError(t)),
                )
            },
        )
    }

    override fun execute(): Response<BottariResult<R>> = throw UnsupportedOperationException()

    override fun clone(): Call<BottariResult<R>> = BottariCall(delegate.clone(), successType)

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    private fun Response<R>.toBottariResult(): BottariResult<R> {
        if (!isSuccessful) return toApiErrorResult()

        val body = body()
        if (body != null) return BottariResult.Success(body)

        return when (code()) {
            201 ->
                extractIdFromHeader()
                    ?.let { BottariResult.Created(it) }
                    ?: BottariResult.ApiError(BottariException.NotFoundCreatedIdException)

            204 -> BottariResult.Success(Unit as R)

            else -> BottariResult.ApiError(BottariException.UnknownException)
        }
    }

    private fun Response<R>.toApiErrorResult(): BottariResult<R> {
        val errorResponse =
            ErrorResponse.parseErrorResponse(errorBody())
                ?: return BottariResult.ApiError(BottariException.UnknownException)

        return runCatching {
            val errorCode = ApiErrorCode.valueOf(errorResponse.title)
            BottariResult.ApiError<R>(errorCode.toException())
        }.getOrElse {
            BottariResult.ApiError(BottariException.UnknownException)
        }
    }

    private fun Response<*>.extractIdFromHeader(): Long? {
        val headerValue = this.headers()["Location"] ?: return null
        return headerValue.takeWhile { it.isDigit() }.toLongOrNull()
    }
}
