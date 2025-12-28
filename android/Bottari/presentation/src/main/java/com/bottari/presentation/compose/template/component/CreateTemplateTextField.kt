package com.bottari.presentation.compose.template.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun CreateTemplateTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    maxLines: Int,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = BottariTheme.typography.medium14.toTextStyle(),
        maxLines = maxLines,
        modifier = modifier.setupCreateTemplateTextField(),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { focusManager.clearFocus() },
            ),
        decorationBox = { innerTextField ->
            CreateTemplateTextFieldDecorationBox(
                value = value,
                placeholder = placeholder,
                innerTextField = innerTextField,
            )
        },
    )
}

@Composable
private fun Modifier.setupCreateTemplateTextField(): Modifier =
    this
        .fillMaxWidth()
        .border(
            width = 1.dp,
            color = BottariTheme.colors.gray400,
            shape = BottariTheme.shapes.radiusMedium,
        )
        .padding(BottariTheme.spacing.spaceMedium)

@Composable
private fun CreateTemplateTextFieldDecorationBox(
    value: String,
    placeholder: String,
    innerTextField: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart,
    ) {
        value.ifEmpty {
            Text(
                text = placeholder,
                style = BottariTheme.typography.medium14.toTextStyle(),
                color = BottariTheme.colors.gray500,
            )
        }
        innerTextField()
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateTemplateTextFieldPreview() {
    var value by remember { mutableStateOf("1234") }

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        CreateTemplateTextField(
            value = value,
            onValueChange = { value = it },
            placeholder = "메시지 입력",
            maxLines = 1,
        )

        CreateTemplateTextField(
            value = "",
            onValueChange = {},
            placeholder = "메시지 입력",
            maxLines = 1,
        )
    }
}
