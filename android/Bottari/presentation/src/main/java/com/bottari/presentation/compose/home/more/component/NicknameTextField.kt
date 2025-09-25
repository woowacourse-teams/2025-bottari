package com.bottari.presentation.compose.home.more.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun NicknameTextField(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSaveNickname: () -> Unit,
    isEditing: Boolean,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChange,
        modifier =
            modifier
                .padding(top = 4.dp)
                .focusRequester(focusRequester)
                .fillMaxWidth(),
        textStyle =
            BottariTheme.typography.medium16
                .toTextStyle()
                .copy(color = BottariTheme.colors.black),
        singleLine = true,
        readOnly = !isEditing,
        cursorBrush = SolidColor(BottariTheme.colors.transparent),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onSaveNickname() }),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (textFieldValue.text.isEmpty() && isEditing) {
                    Text(
                        text = stringResource(R.string.profile_nickname_hint_text),
                        color = BottariTheme.colors.gray500,
                        style = BottariTheme.typography.medium16.toTextStyle(),
                    )
                }
                innerTextField()
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun NicknameTextFieldPreview() {
    var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }
    BottariTheme {
        NicknameTextField(
            textFieldValue = textFieldValue,
            onValueChange = { textFieldValue = it },
            onSaveNickname = {},
            isEditing = true,
            focusRequester = focusRequester,
        )
    }
}
