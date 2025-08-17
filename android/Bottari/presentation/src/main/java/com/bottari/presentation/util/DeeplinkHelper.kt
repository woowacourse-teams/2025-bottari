package com.bottari.presentation.util

import android.net.Uri

object DeeplinkHelper {
    private const val DEEPLINK_URI_SCHEME = "https://"
    private const val DEEPLINK_URI_HOST = "bottari.app"
    private const val DEEPLINK_URI_PATH = "/team"
    private const val KEY_INVITE_CODE = "code"
    private const val QUERY_PREFIX = "?"
    private const val PARAM_ASSIGN = "="

    fun validateUri(uri: Uri?): Boolean = uri != null && uri.host == DEEPLINK_URI_HOST && uri.path == DEEPLINK_URI_PATH

    fun getInviteCode(uri: Uri): String? = uri.getQueryParameter(KEY_INVITE_CODE)

    fun createDeeplink(inviteCode: String): String =
        buildString {
            append(DEEPLINK_URI_SCHEME)
            append(DEEPLINK_URI_HOST)
            append(DEEPLINK_URI_PATH)
            append(QUERY_PREFIX)
            append(KEY_INVITE_CODE)
            append(PARAM_ASSIGN)
            append(inviteCode)
        }
}
