package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

/**
 * 태그나 카테고리를 표시하는 칩 컴포넌트로 선택 상태와 삭제 버튼을 지원합니다.
 *
 * @param text 칩에 표시할 문자열.
 * @param modifier 칩 래퍼에 적용할 Modifier.
 * @param onRemove 삭제 아이콘 터치 시 호출되는 콜백, null이면 아이콘을 숨깁니다.
 * @param selected true면 강조 색상으로 표시합니다.
 */
@Composable
fun BottariInputChip(
    text: String,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    selected: Boolean = false,
) {
    val colors =
        if (selected) {
            BottariTheme.colors.primary to BottariTheme.colors.white
        } else {
            BottariTheme.colors.primarySoft to BottariTheme.colors.primary
        }

    Surface(
        modifier = modifier,
        shape = BottariTheme.shapes.pill,
        color = colors.first,
        contentColor = colors.second,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier =
                Modifier.padding(
                    horizontal = BottariTheme.spacing.spaceXSmall,
                    vertical = BottariTheme.spacing.space2xSmall,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                style = BottariTheme.typography.medium12.toTextStyle(),
            )
            if (onRemove != null) {
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "삭제",
                    modifier =
                        Modifier
                            .size(12.dp)
                            .clip(BottariTheme.shapes.circle)
                            .clickable { onRemove() },
                )
            }
        }
    }
}

/**
 * 여러 개의 BottariInputChip을 행 단위로 배치하고 삭제 콜백을 연결합니다.
 *
 * @param chips 표시할 텍스트 리스트.
 * @param onChipsChange 칩 목록이 변경되었을 때 호출되는 콜백.
 * @param modifier FlowRow에 적용할 Modifier.
 */
@Composable
fun BottariChipGroup(
    chips: List<String>,
    onChipsChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BottariTheme.spacing.space2xSmall),
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
    ) {
        chips.forEach { chip ->
            BottariInputChip(
                text = chip,
                onRemove = { onChipsChange(chips - chip) },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BottariInputChipPreview() {
    BottariTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BottariInputChip(text = "#보따리")
            Spacer(Modifier.height(8.dp))
            BottariInputChip(text = "#삭제가능", onRemove = {})
        }
    }
}

@ComponentPreview
@Composable
private fun BottariChipGroupPreview() {
    var chips by remember { mutableStateOf(listOf("#compose", "#android")) }
    BottariTheme {
        BottariChipGroup(
            chips = chips,
            onChipsChange = { chips = it },
            modifier = Modifier.padding(16.dp),
        )
    }
}
