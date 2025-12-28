package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

/**
 * 진행률을 막대 형태로 보여주는 가로형 프로그레스 컴포넌트입니다.
 *
 * @param progress 0f~1f 범위의 진행 값.
 * @param modifier 배경 박스에 적용할 Modifier.
 * @param color 진행 영역 색상.
 * @param backgroundColor 바탕 영역 색상.
 */
@Composable
fun BottariLinearProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = BottariTheme.colors.primary,
    backgroundColor: Color = BottariTheme.colors.gray200,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BottariTheme.shapes.radiusLarge)
                .background(backgroundColor),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .heightIn(min = 6.dp)
                    .background(color),
        )
    }
}

@ComponentPreview
@Composable
private fun BottariLinearProgressPreview() {
    BottariTheme {
        BottariLinearProgress(progress = 0.4f, modifier = Modifier)
    }
}
