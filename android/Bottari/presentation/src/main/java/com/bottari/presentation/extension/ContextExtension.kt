package com.bottari.presentation.extension

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

@SuppressLint("HardwareIds")
fun Context.getSSAID(): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
