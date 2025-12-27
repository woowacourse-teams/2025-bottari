package com.bottari.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun CollapsedListLine(
    items: List<String>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = BottariTheme.typography.medium14.toTextStyle(),
    separator: String = ", ",
    tailFormat: (Int) -> String = { n -> " 외 ${n}개" },
) {
    val textMeasurer = rememberTextMeasurer()

    SubcomposeLayout(modifier) { constraints ->
        val maxWidthPx = constraints.maxWidth

        fun measureWidth(text: String): Int = textMeasurer.measure(text, style = textStyle, maxLines = 1).size.width

        val sb = StringBuilder()
        var shown = 0
        var currentWidth = 0
        var lastText = ""

        while (shown < items.size) {
            val next = if (shown == 0) items[shown] else separator + items[shown]
            val w = measureWidth(next)
            if (currentWidth + w <= maxWidthPx) {
                sb.append(next)
                currentWidth += w
                shown++
                lastText = sb.toString()
            } else {
                break
            }
        }

        val finalText =
            if (shown < items.size) {
                val remain = items.size - shown
                val tail = tailFormat(remain)
                var tmpShown = shown
                var base = lastText
                var baseWidth = currentWidth
                val tailWidth = measureWidth(tail)

                while (tmpShown > 0 && baseWidth + tailWidth > maxWidthPx) {
                    tmpShown--
                    base =
                        buildString {
                            for (i in 0 until tmpShown) {
                                if (i > 0) append(separator)
                                append(items[i])
                            }
                        }
                    baseWidth = measureWidth(base)
                }
                (if (tmpShown == 0) "" else base) + tail
            } else {
                lastText
            }

        val placeable =
            subcompose("text") {
                Text(
                    text = finalText,
                    style = textStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
            }.first().measure(constraints)

        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdaptiveItemSummaryTextPreview() {
    BottariTheme {
        Column(modifier = Modifier.width(300.dp)) {
            CollapsedListLine(
                items = List(10) { "item $it" },
            )
        }
    }
}
