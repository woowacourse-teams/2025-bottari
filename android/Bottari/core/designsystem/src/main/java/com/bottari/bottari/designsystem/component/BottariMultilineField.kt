package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

/**
 * 긴 문장을 입력할 때 사용하는 멀티라인 필드로 카운터 표시를 지원합니다.
 *
 * @param value 입력 값.
 * @param onValueChange 변경 콜백.
 * @param modifier 외부 Modifier.
 * @param placeholder 값이 비었을 때 보여줄 안내 문구.
 * @param maxLines 표시할 최대 줄 수.
 * @param counter 현재 글자수/제한 정보를 전달하는 Pair, null이면 숨깁니다.
 */
@Composable
fun BottariMultilineField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String,
    maxLines: Int = 4,
    counter: Pair<Int, Int>? = null,
) {
    Box(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = BottariTheme.typography.medium14.toTextStyle(),
            maxLines = maxLines,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = BottariTheme.colors.gray300,
                        shape = BottariTheme.shapes.radiusMedium,
                    ).padding(BottariTheme.spacing.spaceMedium),
            decorationBox = { inner ->
                Box {
                    value.ifBlank {
                        Text(
                            text = placeholder,
                            color = BottariTheme.colors.gray500,
                            style = BottariTheme.typography.medium14.toTextStyle(),
                        )
                    }
                    inner()
                }
            },
        )

        counter?.let { (count, limit) ->
            Text(
                text = "$count/$limit",
                style = BottariTheme.typography.medium12.toTextStyle(),
                color = BottariTheme.colors.gray700,
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(BottariTheme.spacing.space2xSmall)
                        .background(
                            color = BottariTheme.colors.gray200,
                            shape = BottariTheme.shapes.pill,
                        ).padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BottariMultilineFieldPreview() {
    var desc by remember { mutableStateOf("") }
    BottariTheme {
        BottariMultilineField(
            value = desc,
            onValueChange = { desc = it.take(30) },
            placeholder = "설명을 입력하세요",
            counter = desc.length to 30,
            modifier = Modifier.padding(16.dp),
        )
    }
}
