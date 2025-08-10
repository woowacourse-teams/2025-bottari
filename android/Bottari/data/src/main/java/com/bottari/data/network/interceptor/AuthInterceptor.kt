package com.bottari.data.network.interceptor

import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
) : Interceptor {
    private var cachedMemberId: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest =
            chain
                .request()
                .newBuilder()
                .addHeader(IDENTIFIER_HEADER, safeGetMemberId())
                .build()

        return chain.proceed(newRequest)
    }

    private fun safeGetMemberId(): String {
        cachedMemberId?.let { return it }

        return runBlocking(Dispatchers.IO) {
            val memberId =
                memberIdentifierLocalDataSource
                    .getMemberIdentifier()
                    .getOrDefault(DEFAULT_IDENTIFIER)
            cachedMemberId = memberId
            memberId
        }
    }

    companion object {
        private const val IDENTIFIER_HEADER = "ssaid"
        private const val DEFAULT_IDENTIFIER = ""
    }
}
