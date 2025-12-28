package com.bottari.bottari.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class BottariShape(
    val radiusXSmall: Shape = RoundedCornerShape(4.dp),
    val radiusSmall: Shape = RoundedCornerShape(8.dp),
    val radiusMedium: Shape = RoundedCornerShape(12.dp),
    val radiusLarge: Shape = RoundedCornerShape(16.dp),
    val radiusXLarge: Shape = RoundedCornerShape(24.dp),
    val circle: Shape = CircleShape,
    val pill: Shape = RoundedCornerShape(999.dp),
)

val BottariShapes = BottariShape()

val LocalBottariShape = staticCompositionLocalOf { BottariShapes }
