package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

enum class BottariIconButtonTone { None, Primary }

@Composable
fun BottariIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: BottariIconButtonTone = BottariIconButtonTone.None,
    shape: Shape = RoundedCornerShape(12.dp),
    content: @Composable () -> Unit,
) {
    val background =
        bottariIconButtonBackground(
            enabled = enabled,
            tone = tone,
        )

    IconButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        modifier =
            modifier.background(
                color = background,
                shape = shape,
            ),
    ) {
        content()
    }
}

@Composable
private fun bottariIconButtonBackground(
    enabled: Boolean,
    tone: BottariIconButtonTone,
): Color {
    val baseBackground = bottariIconButtonBaseBackground(tone)
    if (enabled) return baseBackground

    return baseBackground.copy(alpha = baseBackground.alpha * 0.6f)
}

@Composable
private fun bottariIconButtonBaseBackground(tone: BottariIconButtonTone): Color =
    when (tone) {
        BottariIconButtonTone.None -> BottariTheme.colors.transparent
        BottariIconButtonTone.Primary -> BottariTheme.colors.primary.copy(alpha = 0.15f)
    }

@ComponentPreview
@Composable
private fun BottariIconButtonPreview() {
    BottariTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BottariIconButton(onClick = {}, tone = BottariIconButtonTone.None) {
                Icon(Icons.Default.Close, null)
            }

            BottariIconButton(onClick = {}, tone = BottariIconButtonTone.Primary) {
                Icon(Icons.Default.Bookmark, null)
            }

            BottariIconButton(
                onClick = {},
                tone = BottariIconButtonTone.Primary,
                enabled = false,
            ) {
                Icon(Icons.Default.Bookmark, null)
            }
        }
    }
}
