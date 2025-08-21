package com.bottari.data.remote

import com.bottari.data.BuildConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class FirebaseRemoteConfigImpl : RemoteConfig {
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            remoteConfigSettings {
                minimumFetchIntervalInSeconds =
                    if (BuildConfig.DEBUG) DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS else MINIMUM_FETCH_INTERVAL_IN_SECONDS
            }.also { settings -> setConfigSettingsAsync(settings) }
        }
    }

    override suspend fun getMinUpdateVersionCode(): Int =
        runCatching {
            remoteConfig.fetchAndActivate().await()
            remoteConfig[KEY_MIN_VERSION_CODE].asLong().toInt()
        }.getOrDefault(DEFAULT_VERSION_CODE)

    companion object {
        private const val MINIMUM_FETCH_INTERVAL_IN_SECONDS = 1800L
        private const val DEBUG_MINIMUM_FETCH_INTERVAL_IN_SECONDS = 0L

        private const val KEY_MIN_VERSION_CODE = "AndroidMinVersionCode"
        private const val DEFAULT_VERSION_CODE = 1
    }
}
