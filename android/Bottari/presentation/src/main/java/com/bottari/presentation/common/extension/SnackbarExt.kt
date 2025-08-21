package com.bottari.presentation.common.extension

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

enum class SnackBarDuration(
    val value: Long,
) {
    LONG_DELAY(3500L),
    SHORT_DELAY(2000L),
    VERY_SHORT_DELAY(1500L),
}

fun View.showSnackbar(
    @StringRes messageRes: Int,
    duration: SnackBarDuration = SnackBarDuration.SHORT_DELAY,
    anchor: View? = null,
    onDismiss: (() -> Unit)? = null,
) {
    Snackbar.make(this, messageRes, Snackbar.LENGTH_INDEFINITE).apply {
        anchor?.let { anchorView ->
            this.anchorView = anchorView
        }
        onDismiss?.let { addDismissCallback(it) }
        show()
        this.view.postDelayed({ dismiss() }, duration.value)
    }
}

fun View.showSnackbar(
    message: String,
    duration: SnackBarDuration = SnackBarDuration.SHORT_DELAY,
    onDismiss: (() -> Unit)? = null,
) {
    Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE).apply {
        onDismiss?.let { addDismissCallback(it) }
        show()
        this.view.postDelayed({ dismiss() }, duration.value)
    }
}

private fun Snackbar.addDismissCallback(onDismiss: () -> Unit) =
    addCallback(
        object : Snackbar.Callback() {
            override fun onDismissed(
                transientBottomBar: Snackbar?,
                event: Int,
            ) = onDismiss()
        },
    )
