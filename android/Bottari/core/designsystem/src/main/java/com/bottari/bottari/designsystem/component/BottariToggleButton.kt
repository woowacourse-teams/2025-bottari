package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

enum class BottariToggleButtonTone { None, Primary }

/**
 * 상태를 전환하는 토글 아이콘 버튼으로 톤과 배경을 설정할 수 있습니다.
 *
 * @param checked 현재 선택 상태.
 * @param onCheckedChange 클릭 시 전달할 상태 변경 콜백.
 * @param modifier 레이아웃 Modifier.
 * @param enabled 비활성화 여부.
 * @param tone 배경 톤 설정.
 * @param shape 버튼 모양.
 * @param content checked 상태를 전달받는 콘텐츠 슬롯.
 */
@Composable
fun BottariToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tone: BottariToggleButtonTone = BottariToggleButtonTone.None,
    shape: Shape = BottariTheme.shapes.radiusMedium,
    content: @Composable (checked: Boolean) -> Unit,
) {
    val background =
        bottariToggleButtonBackground(
            checked = checked,
            enabled = enabled,
            tone = tone,
        )

    BottariIconButton(
        onClick = { onCheckedChange(!checked) },
        enabled = enabled,
        shape = shape,
        modifier =
            modifier.background(
                color = background,
                shape = shape,
            ),
    ) {
        content(checked)
    }
}

@Composable
private fun bottariToggleButtonBackground(
    checked: Boolean,
    enabled: Boolean,
    tone: BottariToggleButtonTone,
): Color {
    val baseBackground = bottariToggleButtonBaseBackground(checked, tone)
    if (enabled) return baseBackground

    return baseBackground.copy(alpha = baseBackground.alpha * 0.6f)
}

@Composable
private fun bottariToggleButtonBaseBackground(
    checked: Boolean,
    tone: BottariToggleButtonTone,
): Color =
    when (tone) {
        BottariToggleButtonTone.None -> {
            BottariTheme.colors.transparent
        }

        BottariToggleButtonTone.Primary -> {
            when {
                checked -> BottariTheme.colors.primary.copy(alpha = 0.24f)
                else -> BottariTheme.colors.primary.copy(alpha = 0.15f)
            }
        }
    }

@ComponentPreview
@Composable
private fun BottariToggleButtonPreview() {
    var checked by remember { mutableStateOf(false) }

    BottariTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BottariToggleButton(
                checked = false,
                onCheckedChange = {},
                tone = BottariToggleButtonTone.None,
            ) { isChecked ->
                Icon(
                    imageVector = if (isChecked) Icons.Default.Close else Icons.Default.Close,
                    contentDescription = null,
                )
            }

            BottariToggleButton(
                checked = checked,
                onCheckedChange = { checked = it },
                tone = BottariToggleButtonTone.Primary,
            ) { isChecked ->
                Icon(
                    imageVector = if (isChecked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                )
            }

            BottariToggleButton(
                checked = true,
                onCheckedChange = {},
                tone = BottariToggleButtonTone.Primary,
            ) { isChecked ->
                Icon(
                    imageVector = if (isChecked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                )
            }
        }
    }
}
