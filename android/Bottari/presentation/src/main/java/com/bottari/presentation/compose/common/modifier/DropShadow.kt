package com.bottari.presentation.compose.common.modifier

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
* 사각/라운드 등 임의의 [shape] 실루엣으로 부드러운 드롭 섀도를 그립니다.
*
* - [blur]: 섀도 번짐 정도(픽셀로 변환됨). 0이면 번짐 없음.
* - [offsetX]/[offsetY]: 섀도의 위치 오프셋.
* - [spread]: 실루엣 자체를 사방으로 확장하는 값(번짐과 별개). 중심 기준으로 균등 확장됩니다.
*
* ⚠️ 부모가 클리핑(clipToPadding/clip) 중이면 섀도가 잘릴 수 있습니다.
*/
fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(alpha = 0.25f),
    blur: Dp = 1.dp,
    offsetY: Dp = 1.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
): Modifier =
    composed {
        val density = LocalDensity.current

        val blurPx = with(density) { blur.toPx().coerceAtLeast(0f) }
        val offsetXPx = with(density) { offsetX.toPx() }
        val offsetYPx = with(density) { offsetY.toPx() }
        val spreadPx = with(density) { spread.toPx().coerceAtLeast(0f) }
        val halfSpread = spreadPx * 0.5f

        val paint =
            remember(color, blurPx) {
                Paint().also { p ->
                    p.color = color
                    val fw = p.asFrameworkPaint()
                    fw.isAntiAlias = true
                    fw.maskFilter =
                        if (blurPx > 0f) {
                            BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
                        } else {
                            null
                        }
                }
            }

        drawBehind {
            val expandedSize = Size(size.width + spreadPx, size.height + spreadPx)
            if (expandedSize.width <= 0f || expandedSize.height <= 0f) return@drawBehind

            val outline: Outline = shape.createOutline(expandedSize, layoutDirection, this)

            drawIntoCanvas { canvas ->
                canvas.save()
                canvas.translate(offsetXPx - halfSpread, offsetYPx - halfSpread)
                canvas.drawOutline(outline, paint)
                canvas.restore()
            }
        }
    }
