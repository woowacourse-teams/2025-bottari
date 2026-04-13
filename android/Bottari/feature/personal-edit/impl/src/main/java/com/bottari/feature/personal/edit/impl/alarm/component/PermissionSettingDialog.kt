package com.bottari.feature.personal.edit.impl.alarm.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariButtonStyle
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun PermissionSettingDialog(
    onNavigateClick: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismiss) {
        PermissionSettingDialogContent(
            onNavigateClick = onNavigateClick,
            onDismiss = onDismiss,
            title = title,
            description = description,
            modifier = modifier,
        )
    }
}

@Composable
private fun PermissionSettingDialogContent(
    onNavigateClick: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    BottariCard(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
    ) {
        Column {
            PermissionSettingDialogHeader(
                title = title,
                onDismiss = onDismiss,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = description,
                modifier =
                    Modifier.padding(horizontal = BottariTheme.spacing.spaceMedium),
                style =
                    BottariTheme.typography.regular14
                        .toTextStyle()
                        .copy(lineHeight = 16.sp),
            )

            PermissionSettingDialogButtons(
                onNavigateClick = onNavigateClick,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun PermissionSettingDialogHeader(
    title: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            modifier =
                Modifier.padding(
                    vertical = BottariTheme.spacing.spaceMedium,
                    horizontal = BottariTheme.spacing.spaceMedium,
                ),
            style = BottariTheme.typography.semiBold20.toTextStyle(),
        )
        BottariIconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "권한 다이얼로그 닫기",
            )
        }
    }
}

@Composable
private fun PermissionSettingDialogButtons(
    onNavigateClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier.padding(
                vertical = BottariTheme.spacing.spaceMedium,
                horizontal = BottariTheme.spacing.spaceMedium,
            ),
    ) {
        BottariButton(
            onClick = onDismiss,
            text = "나중에 하기",
            style = BottariButtonStyle.Secondary,
            modifier = Modifier.weight(1f),
        )

        Spacer(modifier = Modifier.weight(0.1f))

        BottariButton(
            onClick = {
                onNavigateClick()
                onDismiss()
            },
            text = "설정으로 가기",
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview
@Composable
private fun SettingDialogContentPreview() {
    BottariTheme {
        PermissionSettingDialogContent(
            onNavigateClick = {},
            onDismiss = {},
            title = "권한 안내",
            description = "알람을 설정하려면 권한이 필요해요.\n설정 화면으로 이동할까요?",
        )
    }
}
