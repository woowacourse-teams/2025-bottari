package com.bottari.presentation.compose.home.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun BottariCreateDialog(
    title: String,
    subTitle: String,
    btnText: String,
    text: String,
    onChangeText: (String) -> Unit,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isClickable: Boolean = true,
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
    ) {
        BottariBox(
            modifier =
            modifier,
        ) {
            Column {
                Text(text = title, style = BottariTheme.typography.medium16.toTextStyle())
                Spacer(modifier = Modifier.height(BottariTheme.spacing.space2xSmall))
                Text(
                    text = subTitle,
                    style = BottariTheme.typography.regular12.toTextStyle(),
                )
                Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceSmall))
                TextField(
                    modifier =
                        Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                    colors =
                        TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = BottariTheme.colors.gray200,
                            focusedContainerColor = BottariTheme.colors.gray200,
                        ),
                    shape = RoundedCornerShape(8.dp),
                    value = text,
                    onValueChange = { text -> onChangeText(text) },
                    placeholder = {
                        Text(
                            text = placeholder,
                            style = BottariTheme.typography.medium16.toTextStyle(),
                        )
                    },
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(BottariTheme.spacing.spaceMedium))
                Button(
                    modifier =
                        Modifier
                            .height(48.dp)
                            .fillMaxWidth(),
                    onClick = onClick,
                    enabled = isClickable,
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = BottariTheme.colors.primary,
                            disabledContainerColor = BottariTheme.colors.gray400,
                            contentColor = Color.White,
                        ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = btnText, style = BottariTheme.typography.semiBold16.toTextStyle())
                }
            }
        }
    }
}

@Preview
@Composable
fun MyCustomDialogContentPreview() {
    BottariCreateDialog(
        text = "",
        onChangeText = {},
        onClick = {},
        placeholder = stringResource(R.string.bottari_create_default_title_text),
        title = stringResource(R.string.bottari_create_dialog_title_text),
        subTitle = stringResource(R.string.bottari_create_dialog_description_text),
        btnText = stringResource(R.string.bottari_create_dialog_btn_text),
        onDismiss = {},
        isClickable = true,
    )
}
