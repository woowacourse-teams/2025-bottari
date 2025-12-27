package com.bottari.presentation.compose.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.component.BottariBox
import com.bottari.presentation.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TeamSendRemindDialog(
    title: String,
    isRemindable: Boolean,
    onDismissRequest: () -> Unit,
    onClickRemind: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        BottariBox(
            modifier = modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(BottariTheme.spacing.spaceLarge),
        ) {
            Column {
                Text(text = title, style = BottariTheme.typography.bold20.toTextStyle())
                Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
                content()
                if (isRemindable) {
                    Spacer(Modifier.height(BottariTheme.spacing.spaceMedium))
                    Button(
                        onClick = onClickRemind,
                        shape = RoundedCornerShape(16.dp),
                        colors =
                            ButtonColors(
                                BottariTheme.colors.primary,
                                contentColor = BottariTheme.colors.white,
                                disabledContainerColor = BottariTheme.colors.primary,
                                disabledContentColor = BottariTheme.colors.white,
                            ),
                        contentPadding = PaddingValues(BottariTheme.spacing.spaceMedium),
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
