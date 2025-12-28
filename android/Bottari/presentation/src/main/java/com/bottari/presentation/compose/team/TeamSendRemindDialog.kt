package com.bottari.presentation.compose.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.presentation.R

@Composable
fun TeamSendRemindDialog(
    title: String,
    enableRemind: Boolean,
    onDismissRequest: () -> Unit,
    onClickRemind: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BottariCard(modifier = modifier.fillMaxWidth(0.85f)) {
            Column {
                Text(text = title, style = BottariTheme.typography.bold20.toTextStyle())
                Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
                content()
                if (enableRemind) {
                    Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
                    BottariButton(
                        text = "지금 보채기",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onClickRemind,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.iv_notification),
                                contentDescription = null,
                                tint = BottariTheme.colors.white,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamSendRemindDialogPreview() {
    TeamSendRemindDialog(title = "시아", true, {}, {}) {
        TeamStateListBox(
            text = "해당 물건을 챙겼습니다",
            painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
            color = BottariTheme.colors.primary,
            items = listOf("하나", "둘", "셋", "넷", "다섯"),
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceXSmall))
        TeamStateListBox(
            text = "해당 물건을 챙기지 않았습니다.",
            painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
            color = BottariTheme.colors.red,
            items = listOf("하나", "둘", "셋", "넷", "다섯"),
        )
    }
}
