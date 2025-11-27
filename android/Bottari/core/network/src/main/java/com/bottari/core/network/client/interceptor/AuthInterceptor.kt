package com.bottari.core.network.client.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val installationIdProvider: InstallationIdProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val memberIdentifier =
            installationIdProvider.getInstallationId().getOrNull()
                ?: return chain.proceed(chain.request())

        val newRequest =
            chain
                .request()
                .newBuilder()
                .addHeader(IDENTIFIER_HEADER, memberIdentifier)
                .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val IDENTIFIER_HEADER = "ssaid"
    }
}
