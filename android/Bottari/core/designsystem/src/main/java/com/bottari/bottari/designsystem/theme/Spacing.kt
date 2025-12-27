package com.bottari.bottari.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class BottariSpacing(
    val space2xSmall: Dp = 4.dp,
    val spaceXSmall: Dp = 8.dp,
    val spaceSmall: Dp = 12.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceLarge: Dp = 20.dp,
    val spaceXLarge: Dp = 24.dp,
    val space2xLarge: Dp = 28.dp,
)

val LocalBottariSpacing = staticCompositionLocalOf { BottariSpacing() }
