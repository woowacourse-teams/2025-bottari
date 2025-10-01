package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun MyBottariDialogs(
    dialogType: MyBottariDialogType?,
    text: String,
    onChangeText: (String) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
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
        placeholder = placeholder,
        isClickable = isClickable,
        onChangeText = onChangeText,
        onClickBtn = onClick,
        onDismiss = onDismiss,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = BottariTheme.spacing.space2xLarge),
    )
}
