package com.bottari.common.util

import android.net.Uri

object DeeplinkHelper {
    //    private val BASE_URI = BuildConfig.BASE_URL.toUri()
    private val BASE_URI = Uri.parse("https://bottari.app")
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

    fun getInviteCode(uriString: String): String? = runCatching { getInviteCode(Uri.parse(uriString)) }.getOrNull()

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
