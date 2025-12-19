package com.bottari.core.designsystem.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier =
    composed {
        this.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
        ) {
            if (enabled) onClick()
        }
    }
