package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.extension.dropShadow
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

enum class BottariCardTone { Primary, Secondary }

@Composable
fun BottariCard(
    modifier: Modifier = Modifier,
    tone: BottariCardTone = BottariCardTone.Secondary,
    contentPadding: PaddingValues = PaddingValues(BottariTheme.spacing.spaceMedium),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val background =
        when (tone) {
            BottariCardTone.Primary -> BottariTheme.colors.primary
            BottariCardTone.Secondary -> BottariTheme.colors.white
        }

    Box(
        modifier =
            modifier
                .dropShadow(shape = BottariTheme.shapes.radiusMedium)
                .clip(shape = BottariTheme.shapes.radiusMedium)
                .background(color = background)
                .clickableCard(
                    enabled = onClick != null,
                    tone = tone,
                ) { onClick?.invoke() }
                .padding(contentPadding),
        content = content,
    )
}

@Composable
private fun Modifier.clickableCard(
    enabled: Boolean,
    tone: BottariCardTone,
    onClick: () -> Unit,
): Modifier =
    this.clickable(
        onClick = onClick,
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication =
            ripple(
                bounded = true,
                color =
                    when (tone) {
                        BottariCardTone.Primary -> BottariTheme.colors.primary
                        BottariCardTone.Secondary -> BottariTheme.colors.gray700
                    },
            ),
    )

@ComponentPreview
@Composable
private fun BottariCardPreview() {
    BottariTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BottariCard(tone = BottariCardTone.Primary, onClick = {}) {
                Text("Primary Card", color = BottariTheme.colors.white)
            }
            BottariCard(tone = BottariCardTone.Secondary, onClick = {}) {
                Text("Secondary Card")
            }
        }
    }
}
