package com.bottari.feature.mybottari.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.feature.mybottari.R

@Composable
fun MyBottariDialogs(
    dialogType: MyBottariDialogType,
    text: String,
    onChangeText: (String) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    defaultBottariTitle: String,
) {
    val title =
        when (dialogType) {
            MyBottariDialogType.PERSONAL, MyBottariDialogType.TEAM -> stringResource(R.string.bottari_create_dialog_title_text)
            MyBottariDialogType.CODE -> stringResource(R.string.team_bottari_join_dialog_title_text)
        }

    val subTitle =
        when (dialogType) {
            MyBottariDialogType.PERSONAL, MyBottariDialogType.TEAM -> stringResource(R.string.bottari_create_dialog_description_text)
            MyBottariDialogType.CODE -> stringResource(R.string.team_bottari_join_dialog_description_text)
        }

    val btnText =
        when (dialogType) {
            MyBottariDialogType.PERSONAL, MyBottariDialogType.TEAM -> stringResource(R.string.bottari_create_dialog_btn_text)
            MyBottariDialogType.CODE -> stringResource(R.string.team_bottari_join_dialog_btn_text)
        }

    val placeholder =
        when (dialogType) {
            MyBottariDialogType.PERSONAL, MyBottariDialogType.TEAM -> defaultBottariTitle
            MyBottariDialogType.CODE -> ""
        }

    val isClickable =
        when (dialogType) {
            MyBottariDialogType.PERSONAL, MyBottariDialogType.TEAM -> true
            MyBottariDialogType.CODE -> text.isNotEmpty()
        }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        BottariCreateDialog(
            title = title,
            subTitle = subTitle,
            btnText = btnText,
            text = text,
            placeholder = placeholder,
            isClickable = isClickable,
            onChangeText = onChangeText,
            onClickBtn = onClick,
            onDismissRequest = onDismiss,
        )
    }
}

@Preview
@Composable
fun MyBottariDialogsPreview() {
    MyBottariDialogs(
        dialogType = MyBottariDialogType.PERSONAL,
        text = "",
        onChangeText = {},
        onClick = {},
        onDismiss = {},
        defaultBottariTitle = "새 보따리",
    )
}
