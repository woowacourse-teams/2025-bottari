package com.bottari.feature.more.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.feature.more.R

@Composable
fun NicknameTextField(
    textFieldState: TextFieldState,
    isEditing: Boolean,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    TextField(
        state = textFieldState,
        modifier =
            modifier
                .focusRequester(focusRequester)
                .height(20.dp),
        textStyle = BottariTheme.typography.medium16.toTextStyle(),
        readOnly = !isEditing,
        inputTransformation = InputTransformation.maxLength(10),
        lineLimits = TextFieldLineLimits.SingleLine,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onKeyboardAction = KeyboardActionHandler { },
        colors =
            TextFieldDefaults.colors(
                focusedIndicatorColor = BottariTheme.colors.transparent,
                unfocusedIndicatorColor = BottariTheme.colors.transparent,
                disabledIndicatorColor = BottariTheme.colors.transparent,
                focusedTextColor = BottariTheme.colors.black,
                unfocusedTextColor = BottariTheme.colors.black,
                disabledTextColor = BottariTheme.colors.black,
                focusedContainerColor = BottariTheme.colors.transparent,
                unfocusedContainerColor = BottariTheme.colors.transparent,
                disabledContainerColor = BottariTheme.colors.transparent,
                cursorColor = BottariTheme.colors.black,
            ),
        contentPadding = PaddingValues(0.dp),
        placeholder = {
            textFieldState.text.ifEmpty {
                Text(
                    text = stringResource(R.string.profile_nickname_hint_text),
                    color = BottariTheme.colors.gray500,
                    style = BottariTheme.typography.medium16.toTextStyle(),
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun NicknameTextFieldPreview() {
    val focusRequester = remember { FocusRequester() }
    val textFieldState = rememberTextFieldState()

    BottariTheme {
        NicknameTextField(
            textFieldState = textFieldState,
            isEditing = true,
            focusRequester = focusRequester,
        )
    }
}
