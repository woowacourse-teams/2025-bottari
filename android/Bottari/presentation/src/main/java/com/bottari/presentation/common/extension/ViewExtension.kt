package com.bottari.presentation.common.extension

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

fun View.applyImeBottomPadding() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
        val bottomInset = calculateBottomInset(insets, imeVisible)
        view.setPadding(0, 0, 0, bottomInset)
        insets
    }
}

private fun calculateBottomInset(
    insets: WindowInsetsCompat,
    imeVisible: Boolean,
): Int {
    if (!imeVisible) return 0

    val imeInset = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
    val systemBarInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

    return (imeInset - systemBarInset).coerceAtLeast(0)
}
