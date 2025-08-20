package com.bottari.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseRemoteConfig : RemoteConfig {
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL_IN_SECONDS
            }.also { settings -> setConfigSettingsAsync(settings) }
        }
    }

    override suspend fun getMinUpdateVersionCode(): Int =
        getValue(KEY_MIN_VERSION_CODE)
            ?.asLong()
            ?.toInt()
            ?: DEFAULT_VERSION_CODE

    private suspend fun getValue(key: String): FirebaseRemoteConfigValue? =
        suspendCoroutine { continuation ->
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(remoteConfig[key])
                    return@addOnCompleteListener
                }
                continuation.resume(null)
            }
        }

    companion object {
        private const val MINIMUM_FETCH_INTERVAL_IN_SECONDS = 60L

        private const val KEY_MIN_VERSION_CODE = "AndroidMinVersionCode"
        private const val DEFAULT_VERSION_CODE = 1
    }
}
