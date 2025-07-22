package com.bottari.presentation.extension

import android.content.Context

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
