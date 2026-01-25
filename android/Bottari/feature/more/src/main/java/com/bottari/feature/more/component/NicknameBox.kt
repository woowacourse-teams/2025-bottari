package com.bottari.feature.more.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.feature.more.R

@Composable
fun NicknameBox(
    nickname: String,
    onChangeNickname: (String) -> Unit,
    onSaveNickname: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isEditing by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val textFieldState = rememberTextFieldState(initialText = nickname)

    LaunchedEffect(nickname, isEditing) {
        if (nickname != textFieldState.text.toString()) {
            textFieldState.setTextAndPlaceCursorAtEnd(nickname)
        }
    }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
            keyboardController?.show()
            return@LaunchedEffect
        }
        keyboardController?.hide()
        focusRequester.freeFocus()
    }

    BottariCard(
        modifier = modifier,
        contentPadding =
            PaddingValues(
                top = BottariTheme.spacing.spaceSmall,
                bottom = BottariTheme.spacing.spaceXSmall,
                start = BottariTheme.spacing.spaceMedium,
                end = BottariTheme.spacing.space2xSmall,
            ),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.profile_nickname_title),
                    style = BottariTheme.typography.semiBold20.toTextStyle(),
                )
                NicknameTextField(
                    textFieldState = textFieldState,
                    isEditing = isEditing,
                    focusRequester = focusRequester,
                )
            }

            NicknameEditButton(
                isEditing = isEditing,
                onClick = {
                    if (isEditing) {
                        onChangeNickname(textFieldState.text.toString())
                        onSaveNickname()
                    }
                    isEditing = !isEditing
                },
            )
        }
    }
}

@Composable
private fun NicknameEditButton(
    isEditing: Boolean,
    onClick: () -> Unit,
) {
    BottariIconButton(onClick = onClick) {
        if (isEditing) {
            Icon(
                imageVector = Icons.Default.Check,
                tint = BottariTheme.colors.green,
                contentDescription = stringResource(R.string.btn_nickname_edit_description),
            )
            return@BottariIconButton
        }

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = stringResource(R.string.btn_nickname_edit_description),
        )
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
