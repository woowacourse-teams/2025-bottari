package com.bottari.data.network.adapter

import com.bottari.domain.model.exception.BottariResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class BottariCallAdapter<R>(
    private val successType: Type,
) : CallAdapter<R, Call<BottariResult<R>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<R>): Call<BottariResult<R>> = BottariCall(call, successType)
}
