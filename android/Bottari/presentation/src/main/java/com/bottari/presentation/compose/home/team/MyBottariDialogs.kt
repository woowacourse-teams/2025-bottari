package com.bottari.presentation.compose.home.team

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bottari.presentation.R

@Composable
fun MyBottariDialogs(
    dialogType: MyBottariDialogType?,
    onChangeText: (String) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    text: String,
    defaultBottariTitle: String,
) {
    if (dialogType == null) return

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

    BottariCreateDialog(
        title = title,
        subTitle = subTitle,
        btnText = btnText,
        text = text,
        onChangeText = onChangeText,
        onClick = onClick,
        onDismiss = onDismiss,
        placeholder = placeholder,
        isClickable = isClickable,
    )
}
