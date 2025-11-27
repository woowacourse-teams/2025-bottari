package com.bottari.core.network.client.interceptor

interface InstallationIdProvider {
    fun getInstallationId(): Result<String>
}
