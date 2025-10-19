package com.bottari.presentation.compose.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun ChecklistToolTip(
    icon: @Composable () -> Unit,
    title: String,
    text: String,
    closeAction: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = BottariTheme.colors.primary)
                .padding(16.dp),
    ) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier
                        .clip(CircleShape)
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
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = closeAction),
                tint = BottariTheme.colors.white,
            )
        }
        Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = text,
            style =
                BottariTheme.typography.regular16
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
                painter = painterResource(R.drawable.ic_team_invite_code),
                contentDescription = null,
                tint = BottariTheme.colors.white,
            )
        },
        title = "개인 보따리",
        text =
            "오른쪽 상단 버튼을 통해,\n" +
                "스와이프 화면으로 이동할 수 있어요.",
        closeAction = {},
    )
}
