package com.bottari.core.ui.modifier

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class FadeSide {
    LEFT,
    RIGHT,
    BOTTOM,
    TOP,
}

fun Modifier.leftFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null,
) = fadingEdge(FadeSide.LEFT, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.rightFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null,
) = fadingEdge(FadeSide.RIGHT, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.topFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null,
) = fadingEdge(FadeSide.TOP, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.bottomFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null,
) = fadingEdge(FadeSide.BOTTOM, color = color, width = width, isVisible = isVisible, spec = spec)

fun Modifier.topBottomFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null,
) = topFadingEdge(color = color, isVisible = isVisible, width = width, spec = spec)
    .bottomFadingEdge(color = color, isVisible = isVisible, width = width, spec = spec)

fun Modifier.startEndFadingEdge(
    color: Color,
    isVisible: Boolean = true,
    width: Dp = 16.dp,
    spec: AnimationSpec<Dp>? = null,
) = leftFadingEdge(color = color, isVisible = isVisible, width = width, spec = spec)
    .rightFadingEdge(color = color, isVisible = isVisible, width = width, spec = spec)

fun Modifier.fadingEdge(
    vararg sides: FadeSide,
    color: Color,
    width: Dp,
    isVisible: Boolean,
    spec: AnimationSpec<Dp>?,
) = composed {
    require(width > 0.dp) { "유효하지 않은 Fade 너비: 너비는 0보다 커야 합니다" }

    val animatedWidth =
        spec?.let {
            animateDpAsState(
                targetValue = if (isVisible) width else 0.dp,
                animationSpec = spec,
                label = "Fade width",
            ).value
        }

    drawWithContent {
        this@drawWithContent.drawContent()

        sides.forEach { side ->
            val (start, end) = this.size.getFadeOffsets(side)

            val staticWidth = if (isVisible) width.toPx() else 0f
            val widthPx = animatedWidth?.toPx() ?: staticWidth

            val fraction =
                when (side) {
                    FadeSide.LEFT, FadeSide.RIGHT -> widthPx / this.size.width
                    FadeSide.BOTTOM, FadeSide.TOP -> widthPx / this.size.height
                }

            drawRect(
                brush =
                    Brush.linearGradient(
                        0f to color,
                        fraction to Color.Transparent,
                        start = start,
                        end = end,
                    ),
                size = this.size,
            )
        }
    }
}

private fun Size.getFadeOffsets(side: FadeSide): Pair<Offset, Offset> =
    when (side) {
        FadeSide.LEFT -> Offset.Zero to Offset(width, 0f)
        FadeSide.RIGHT -> Offset(width, 0f) to Offset.Zero
        FadeSide.BOTTOM -> Offset(0f, height) to Offset.Zero
        FadeSide.TOP -> Offset.Zero to Offset(0f, height)
    }
