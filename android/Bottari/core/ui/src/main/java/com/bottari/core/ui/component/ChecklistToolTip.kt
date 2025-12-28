package com.bottari.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R

@Composable
fun ChecklistToolTip(
    icon: @Composable () -> Unit,
    title: String,
    text: String,
    closeAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BottariTheme.shapes.radiusMedium)
                .background(color = BottariTheme.colors.primary)
                .padding(
                    top = BottariTheme.spacing.spaceXSmall,
                    start = BottariTheme.spacing.spaceMedium,
                    end = BottariTheme.spacing.spaceMedium,
                    bottom = BottariTheme.spacing.spaceMedium,
                ),
    ) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .clip(BottariTheme.shapes.circle)
                        .size(32.dp)
                        .background(color = BottariTheme.colors.white.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center,
            ) {
                icon()
            }
            Spacer(modifier = Modifier.width(BottariTheme.spacing.spaceSmall))
            Text(
                text = title,
                style = BottariTheme.typography.medium16.toTextStyle(),
                color = BottariTheme.colors.white,
            )
            Spacer(modifier = Modifier.weight(1f))
            BottariIconButton(onClick = closeAction) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.common_close_btn_description),
                    tint = BottariTheme.colors.white,
                )
            }
        }
        Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))
        Text(
            text = text,
            style =
                BottariTheme.typography.regular14
                    .toTextStyle()
                    .copy(lineHeight = 20.sp),
            color = BottariTheme.colors.white,
        )
    }
}

@Preview
@Composable
private fun ChecklistToolTipPreview() {
    ChecklistToolTip(
        icon = {
            Icon(
                imageVector = Icons.Default.Code,
                contentDescription = null,
                tint = BottariTheme.colors.white,
            )
        },
        title = "개인 보따리",
        text = "오른쪽 상단 버튼을 통해,\n스와이프 화면으로 이동할 수 있어요.",
        closeAction = {},
    )
}
