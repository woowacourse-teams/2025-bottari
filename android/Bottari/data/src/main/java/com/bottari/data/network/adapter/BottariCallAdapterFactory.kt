package com.bottari.data.network.adapter

import com.bottari.domain.model.exception.BottariResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class BottariCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        // Call<T> 형태의 반환 타입인지 확인
        if (getRawType(returnType) != Call::class.java) return null

        // 반환 타입이 제네릭인지 확인
        check(returnType is ParameterizedType) { "반환 타입은 Call<*>로 정의되어야 합니다." }

        // Call<T>에서 T 추출
        val responseType = getParameterUpperBound(0, returnType)

        // BottariResult<T> 형태의 반환 타입인지 확인
        if (getRawType(responseType) != BottariResult::class.java) return null

        // 반환 타입이 제네릭인지 확인
        check(responseType is ParameterizedType) { "반환 타입은 BottariResult<*>로 정의되어야 합니다" }

        // BottariResult<T>에서 T 추출
        val bodyType = getParameterUpperBound(0, responseType)
        return BottariCallAdapter<Any>(bodyType)
    }
}
