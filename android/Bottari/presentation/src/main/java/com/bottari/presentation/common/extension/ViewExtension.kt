package com.bottari.presentation.common.extension

import android.view.View
import androidx.core.view.isVisible

fun View.fadeIn(duration: Long = 200L) {
    if (!isVisible || alpha < 1f) {
        animate().cancel()
        visibility = View.VISIBLE
        alpha = 0f
        animate()
            .alpha(1f)
            .setDuration(duration)
            .setListener(null)
    }
}

fun View.fadeOut(duration: Long = 200L) {
    if (isVisible && alpha > 0f) {
        animate().cancel()
        animate()
            .alpha(0f)
            .setDuration(duration)
            .withEndAction {
                visibility = View.GONE
            }
    }
}
