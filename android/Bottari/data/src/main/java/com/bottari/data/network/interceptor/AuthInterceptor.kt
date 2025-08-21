package com.bottari.data.network.interceptor

import com.bottari.data.source.local.MemberIdentifierLocalDataSource
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val memberIdentifierLocalDataSource: MemberIdentifierLocalDataSource,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val memberIdentifier = getMemberIdentifier() ?: return chain.proceed(chain.request())

        val newRequest =
            chain
                .request()
                .newBuilder()
                .addHeader(IDENTIFIER_HEADER, memberIdentifier)
                .build()

        return chain.proceed(newRequest)
    }

    private fun getMemberIdentifier(): String? = memberIdentifierLocalDataSource.getInstallationId().getOrNull()

    companion object {
        private const val IDENTIFIER_HEADER = "ssaid"
    }
}
