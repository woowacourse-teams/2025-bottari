package com.bottari.bottari.designsystem.extension

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = BottariTheme.colors.black.copy(0.25f),
    blur: Dp = 1.dp,
    offsetY: Dp = 1.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
) = composed {
    val density = LocalDensity.current

    val paint =
        remember(color, blur) {
            Paint().apply {
                this.color = color
                val blurPx = with(density) { blur.toPx() }
                if (blurPx > 0f) {
                    this.asFrameworkPaint().maskFilter =
                        BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
        }

    drawBehind {
        val spreadPx = spread.toPx()
        val offsetXPx = offsetX.toPx()
        val offsetYPx = offsetY.toPx()

        val shadowWidth = size.width + spreadPx
        val shadowHeight = size.height + spreadPx

        if (shadowWidth <= 0f || shadowHeight <= 0f) return@drawBehind

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.translate(offsetXPx, offsetYPx)
            canvas.drawOutline(shadowOutline, paint)
            canvas.restore()
        }
    }
}

@Preview(showBackground = true, name = "DropShadow – Basic")
@Composable
private fun DropShadowPreview() {
    BottariTheme {
        val shape = RoundedCornerShape(12.dp)

        Box(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier =
                    Modifier
                        .size(50.dp)
                        .dropShadow(shape = shape)
                        .background(color = BottariTheme.colors.white, shape = shape),
            )
        }
    }
}
