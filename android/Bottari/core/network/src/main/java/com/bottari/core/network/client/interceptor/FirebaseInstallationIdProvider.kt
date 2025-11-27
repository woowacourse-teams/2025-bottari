package com.bottari.core.network.client.interceptor

import com.google.android.gms.tasks.Tasks
import com.google.firebase.installations.FirebaseInstallations
import java.util.concurrent.TimeUnit

class FirebaseInstallationIdProvider(
    private val timeoutSeconds: Long = 10,
) : InstallationIdProvider {
    @Volatile
    private var cachedId: String? = null

    override fun getInstallationId(): Result<String> =
        runCatching {
            cachedId ?: initialize().also { cachedId = it }
        }

    private fun initialize(): String =
        Tasks.await(
            FirebaseInstallations.getInstance().id,
            timeoutSeconds,
            TimeUnit.SECONDS,
        )
}
