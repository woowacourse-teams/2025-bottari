package com.bottari.data.remote

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class FirebaseRemoteConfigImpl : RemoteConfig {
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = MINIMUM_FETCH_INTERVAL_IN_SECONDS
            }.also { settings -> setConfigSettingsAsync(settings) }
        }
    }

    override suspend fun getMinUpdateVersionCode(): Int =
        runCatching {
            remoteConfig.fetchAndActivate().await()
            remoteConfig[KEY_MIN_VERSION_CODE].asLong().toInt()
        }.getOrDefault(DEFAULT_VERSION_CODE)

    companion object {
        private const val MINIMUM_FETCH_INTERVAL_IN_SECONDS = 60L
        private const val KEY_MIN_VERSION_CODE = "AndroidMinVersionCode"
        private const val DEFAULT_VERSION_CODE = 1
    }
}
