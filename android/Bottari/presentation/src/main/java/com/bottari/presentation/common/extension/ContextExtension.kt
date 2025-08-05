package com.bottari.presentation.common.extension

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.bottari.presentation.BuildConfig

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

@SuppressLint("HardwareIds")
fun Context.getSSAID(): String {
    if (BuildConfig.DEBUG) return BuildConfig.DEV_ID
    return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}
