package com.bottari.presentation.compose.common.theme

import androidx.activity.SystemBarStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.bottari.presentation.compose.common.theme.BottariTheme.typography

object BottariTheme {
    val colors: BottariColorSystem
        @Composable
        @ReadOnlyComposable
        get() = LocalBottariColorSystem.current

    val spacing: BottariSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalBottariSpacing.current

    val typography: BottariTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalBottariTypographySystem.current
}

val LocalBottariStatusStyle =
    compositionLocalOf {
        SystemBarStyle.light(
            scrim = Color.Black.toArgb(),
            darkScrim = Color.Black.toArgb(),
        )
    }

@Composable
fun BottariTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalBottariBgColor provides LocalBottariBgColor.current,
        LocalBottariStatusStyle provides LocalBottariStatusStyle.current,
    ) {
        ProvideTextStyle(value = typography.medium14.toTextStyle()) {
            content()
        }
    }
}
