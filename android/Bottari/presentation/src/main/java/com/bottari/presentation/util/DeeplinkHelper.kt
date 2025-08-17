package com.bottari.presentation.util

import android.net.Uri
import androidx.core.net.toUri
import com.bottari.presentation.BuildConfig

object DeeplinkHelper {
    private val BASE_URI = BuildConfig.BASE_URL.toUri()
    private const val DEEPLINK_URI_PATH = "team"
    private const val KEY_INVITE_CODE = "code"

    fun validateUri(uri: Uri?): Boolean =
        uri != null &&
            uri.scheme == BASE_URI.scheme &&
            uri.host == BASE_URI.host &&
            uri.pathSegments.contains(
                DEEPLINK_URI_PATH,
            )

    fun getInviteCode(uri: Uri): String? = uri.getQueryParameter(KEY_INVITE_CODE)

    fun createDeeplink(inviteCode: String): String =
        Uri
            .Builder()
            .scheme(BASE_URI.scheme)
            .authority(BASE_URI.host)
            .appendPath(DEEPLINK_URI_PATH)
            .appendQueryParameter(KEY_INVITE_CODE, inviteCode)
            .build()
            .toString()
}
