package com.bottari.presentation.util

import android.net.Uri

object DeeplinkHelper {
    private const val DEEPLINK_URI_HOST = "bottari.app"
    private const val DEEPLINK_URI_PATH = "/invite"
    private const val KEY_INVITE_CODE = "code"

    fun validateUri(uri: Uri?): Boolean = uri != null && uri.host == DEEPLINK_URI_HOST && uri.path == DEEPLINK_URI_PATH

    fun getInviteCode(uri: Uri): String? = uri.getQueryParameter(KEY_INVITE_CODE)
}
