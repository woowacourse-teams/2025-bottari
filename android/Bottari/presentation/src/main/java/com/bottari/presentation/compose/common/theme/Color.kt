package com.bottari.presentation.compose.common.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class BottariColorSystem(
    val transparent: Color = Color(0x00000000),
    val black: Color = Color(0xFF000000),
    val white: Color = Color(0xFFFFFFFF),
    val primary: Color = Color(0xFF0064FF),
    val red: Color = Color(0xFFFF0000),
    val redSoft: Color = Color(0xFFFF3B30),
    val gray50: Color = Color(0xFFF8F8F8),
    val gray100: Color = Color(0xFFF2F2F5),
    val gray200: Color = Color(0xFFEEEEEE),
    val gray300: Color = Color(0xFFD1DEE8),
    val gray400: Color = Color(0xFFC4C4C4),
    val gray500: Color = Color(0xFFA6A6A6),
    val gray600: Color = Color(0xFF999999),
    val gray700: Color = Color(0xFF787878),
    val productTypePersonal: Color = Color(0xFF0064FF),
    val productTypeShared: Color = Color(0xFF6EBA76),
    val productTypeAssigned: Color = Color(0xFF00AEFF),
)

private val lightColorScheme = BottariColorSystem()

val LocalBottariColorSystem = staticCompositionLocalOf { lightColorScheme }

val LocalBottariBgColor = compositionLocalOf { lightColorScheme.gray50 }
