package com.bottari.presentation.compose.home.more.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun NicknameBox(
    nickname: String,
    onChangeNickname: (String) -> Unit,
    onSaveNickname: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }
    var textFieldValue by remember(nickname) {
        mutableStateOf(TextFieldValue(text = nickname, selection = TextRange(nickname.length)))
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
            keyboardController?.show()
            textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            return@LaunchedEffect
        }
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    BottariBox(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.profile_nickname_title),
                    modifier = Modifier.align(Alignment.Start),
                    color = BottariTheme.colors.black,
                    style = BottariTheme.typography.semiBold20.toTextStyle(),
                )
                NicknameTextField(
                    textFieldValue = textFieldValue,
                    onValueChange = { newValue ->
                        textFieldValue = newValue
                        onChangeNickname(newValue.text)
                    },
                    onSaveNickname = onSaveNickname,
                    isEditing = isEditing,
                    focusRequester = focusRequester,
                )
            }
            IconButton(
                onClick = {
                    if (isEditing) onSaveNickname()
                    isEditing = isEditing.not()
                },
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                val iconRes = if (isEditing) R.drawable.ic_confirm else R.drawable.ic_pen
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = stringResource(R.string.common_btn_nickname_edit_description),
                )
            }
        }
    }
}

@Preview
@Composable
private fun NicknameTextFieldPreview() {
    var nickname by remember { mutableStateOf("오이") }
    BottariTheme {
        NicknameBox(
            nickname = nickname,
            onChangeNickname = { nickname = it },
            onSaveNickname = {},
        )
    }
}
