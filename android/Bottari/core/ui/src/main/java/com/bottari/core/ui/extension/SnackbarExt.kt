package com.bottari.core.ui.extension

import android.view.View
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SnackBarDuration(
    val value: Long,
) {
    LONG_DELAY(3500L),
    SHORT_DELAY(2000L),
    VERY_SHORT_DELAY(1500L),
}

suspend fun CoroutineScope.showSnackbar(
    snackbarState: SnackbarHostState,
    message: String,
    duration: SnackBarDuration = SnackBarDuration.VERY_SHORT_DELAY,
    dismissAction: (() -> Unit)? = null,
) {
    val autoDismissJob =
        launch {
            delay(duration.value)
            snackbarState.currentSnackbarData?.dismiss()
        }

    val result =
        snackbarState.showSnackbar(
            message = message,
            withDismissAction = dismissAction != null,
            duration = SnackbarDuration.Indefinite,
        )

    autoDismissJob.cancel()

    if (result == SnackbarResult.Dismissed) {
        dismissAction?.invoke()
    }
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

private fun Snackbar.addDismissCallback(onDismiss: () -> Unit) =
    addCallback(
        object : Snackbar.Callback() {
            override fun onDismissed(
                transientBottomBar: Snackbar?,
                event: Int,
            ) = onDismiss()
        },
    )
