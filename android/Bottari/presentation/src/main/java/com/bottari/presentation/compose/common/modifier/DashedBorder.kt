package com.bottari.presentation.compose.common.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.dashedBorder(
    color: Color = Color.LightGray,
    strokeWidth: Dp = 1.dp,
    cornerRadius: Dp = 12.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 4.dp,
) = this.then(
    Modifier.drawBehind {
        val strokePx = strokeWidth.toPx()
        val dashWidthPx = dashLength.toPx()
        val dashGapPx = gapLength.toPx()
        val radiusPx = cornerRadius.toPx()

        drawRoundRect(
            color = color,
            size = size,
            cornerRadius = CornerRadius(radiusPx, radiusPx),
            style =
                Stroke(
                    width = strokePx,
                    pathEffect =
                        PathEffect.dashPathEffect(
                            floatArrayOf(dashWidthPx, dashGapPx),
                        ),
                ),
        )
    },
)
