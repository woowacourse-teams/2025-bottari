package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

enum class BottariButtonStyle { Primary, Secondary, None }

/**
 * 보따리 전역에서 사용하는 기본 버튼으로 스타일과 아이콘 조합을 설정할 수 있습니다.
 *
 * @param text 버튼 안에 표시되는 텍스트.
 * @param onClick 버튼 클릭 시 호출되는 콜백.
 * @param modifier 외부에서 전달하는 레이아웃/스타일 Modifier.
 * @param enabled false이면 비활성화 상태로 표시됩니다.
 * @param style 버튼 배경·텍스트 색을 정의하는 스타일.
 * @param leadingIcon 텍스트 앞에 배치되는 아이콘 콘텐츠.
 * @param trailingIcon 텍스트 뒤에 배치되는 아이콘 콘텐츠.
 */
@Composable
fun BottariButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: BottariButtonStyle = BottariButtonStyle.Primary,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val elevationValue = if (style != BottariButtonStyle.None) 1.dp else 0.dp

    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = 48.dp),
        enabled = enabled,
        shape = BottariTheme.shapes.radiusMedium,
        colors = buttonColorsByStyle(style),
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = elevationValue,
                pressedElevation = elevationValue,
            ),
        contentPadding = PaddingValues(horizontal = BottariTheme.spacing.spaceSmall),
    ) {
        leadingIcon?.let { leadingIcon ->
            leadingIcon()
            Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
        }

        Text(
            text = text,
            style = BottariTheme.typography.semiBold16.toTextStyle(),
        )

        trailingIcon?.let { trailingIcon ->
            Spacer(Modifier.width(BottariTheme.spacing.spaceXSmall))
            trailingIcon()
        }
    }
}

@Composable
private fun buttonColorsByStyle(style: BottariButtonStyle) =
    when (style) {
        BottariButtonStyle.Primary -> {
            ButtonDefaults.buttonColors(
                containerColor = BottariTheme.colors.primary,
                disabledContainerColor = BottariTheme.colors.gray400,
                contentColor = BottariTheme.colors.white,
                disabledContentColor = BottariTheme.colors.white,
            )
        }

        BottariButtonStyle.Secondary -> {
            ButtonDefaults.buttonColors(
                containerColor = BottariTheme.colors.white,
                disabledContainerColor = BottariTheme.colors.gray200,
                contentColor = BottariTheme.colors.black,
                disabledContentColor = BottariTheme.colors.gray500,
            )
        }

        BottariButtonStyle.None -> {
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                contentColor = BottariTheme.colors.primary,
                disabledContentColor = BottariTheme.colors.gray400,
            )
        }
    }

@ComponentPreview
@Composable
private fun EnabledBottariButtonPreview() {
    BottariTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            BottariButton(text = "기본", onClick = {})
            BottariButton(text = "기본", onClick = {}, enabled = false)

            BottariButton(text = "Secondary", onClick = {}, style = BottariButtonStyle.Secondary)
            BottariButton(
                text = "Secondary",
                onClick = {},
                style = BottariButtonStyle.Secondary,
                enabled = false,
            )

            BottariButton(
                text = "Ghost",
                onClick = {},
                style = BottariButtonStyle.None,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = { Icon(Icons.Default.Close, null) },
            )
            BottariButton(
                text = "Ghost",
                onClick = {},
                style = BottariButtonStyle.None,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = { Icon(Icons.Default.Close, null) },
                enabled = false,
            )
        }
    }
}
