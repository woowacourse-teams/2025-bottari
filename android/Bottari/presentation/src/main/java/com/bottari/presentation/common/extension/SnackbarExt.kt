package com.bottari.presentation.common.extension

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    @StringRes messageRes: Int,
    onDismiss: (() -> Unit)? = null,
) {
    Snackbar
        .make(this, messageRes, Snackbar.LENGTH_SHORT)
        .apply { onDismiss?.let { addDismissCallback(it) } }
        .show()
}

fun View.showCustomSnackbar(
    @StringRes messageRes: Int,
    durationMillis: Long = 2000L,
    onDismiss: (() -> Unit)? = null,
) {
    Snackbar.make(this, messageRes, Snackbar.LENGTH_INDEFINITE).apply {
        onDismiss?.let { addDismissCallback(it) }
        show()
        this.view.postDelayed({ dismiss() }, durationMillis)
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
