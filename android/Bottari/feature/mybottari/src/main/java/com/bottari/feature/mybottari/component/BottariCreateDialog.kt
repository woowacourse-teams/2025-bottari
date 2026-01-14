package com.bottari.feature.mybottari.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bottari.bottari.designsystem.component.BottariButton
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariTextField
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.feature.mybottari.R

@Composable
fun BottariCreateDialog(
    onDismissRequest: () -> Unit,
    title: String,
    subTitle: String,
    btnText: String,
    text: String,
    placeholder: String,
    isClickable: Boolean,
    onChangeText: (String) -> Unit,
    onClickBtn: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = true),
    ) {
        BottariCard {
            Column {
                Text(text = title, style = BottariTheme.typography.medium16.toTextStyle())
                Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))
                Text(
                    text = subTitle,
                    style = BottariTheme.typography.regular12.toTextStyle(),
                )
                Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
                BottariTextField(
                    value = text,
                    onValueChange = { newText -> onChangeText(newText) },
                    placeholder = placeholder,
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
                BottariButton(
                    text = btnText,
                    onClick = onClickBtn,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isClickable,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyCustomDialogContentPreview() {
    BottariCreateDialog(
        title = stringResource(R.string.bottari_create_dialog_title_text),
        subTitle = stringResource(R.string.bottari_create_dialog_description_text),
        btnText = stringResource(R.string.bottari_create_dialog_btn_text),
        text = "",
        placeholder = stringResource(R.string.bottari_create_default_title_text),
        isClickable = true,
        onChangeText = {},
        onClickBtn = {},
        onDismissRequest = {},
    )
}
