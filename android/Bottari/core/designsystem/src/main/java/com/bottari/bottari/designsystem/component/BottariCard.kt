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

/**
 * 그림자와 둥근 모서리를 가진 보따리 카드 컴포넌트입니다.
 *
 * @param modifier 카드 전체에 적용할 Modifier.
 * @param tone 배경색과 리플 컬러를 결정하는 톤.
 * @param contentPadding 카드 내부 여백.
 * @param onClick null이 아니면 클릭 가능한 카드로 동작합니다.
 * @param content 카드 본문 콘텐츠 슬롯.
 */
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
): Modifier {
    if (!enabled) return this

    return this.clickable(
        onClick = onClick,
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
}

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
