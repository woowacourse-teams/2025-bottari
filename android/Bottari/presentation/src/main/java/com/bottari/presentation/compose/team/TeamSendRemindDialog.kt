package com.bottari.presentation.compose.team

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun TeamSendRemindDialog(
    title: String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        BottariBox(modifier = modifier.fillMaxWidth()) {
            Column {
                Text(text = title, style = BottariTheme.typography.semiBold20.toTextStyle())
                Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
                content()
                Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
                BottariBox(
                    modifier =
                        Modifier
                            .background(BottariTheme.colors.primary)
                            .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.iv_notification),
                            contentDescription = null,
                            tint = BottariTheme.colors.white,
                        )
                        Spacer(Modifier.width(BottariTheme.spacing.space2xSmall))
                        Text(
                            text = "지금 보채기",
                            style = BottariTheme.typography.semiBold16.toTextStyle(),
                            color = BottariTheme.colors.white,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TeamSendRemindDialogPreview() {
    TeamSendRemindDialog(title = "시아", {}) {
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
