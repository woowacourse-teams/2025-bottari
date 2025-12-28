package com.bottari.bottari.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.preview.ComponentPreview
import com.bottari.bottari.designsystem.theme.BottariTheme

@Composable
fun BottariTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    label: String? = null,
    supportingText: String? = null,
    singleLine: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        label?.let {
            Text(
                text = it,
                style = BottariTheme.typography.medium14.toTextStyle(),
                color = BottariTheme.colors.gray600,
                modifier = Modifier.padding(bottom = BottariTheme.spacing.space2xSmall),
            )
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            placeholder = {
                Text(
                    text = placeholder,
                    color = BottariTheme.colors.gray500,
                    style = BottariTheme.typography.regular14.toTextStyle(),
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = isError,
            shape = BottariTheme.shapes.radiusMedium,
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = BottariTheme.colors.gray200,
                    unfocusedContainerColor = BottariTheme.colors.gray200,
                    disabledContainerColor = BottariTheme.colors.gray200,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = BottariTheme.colors.black,
                ),
            textStyle = BottariTheme.typography.regular14.toTextStyle(),
            modifier = Modifier.fillMaxWidth(),
        )

        supportingText?.let {
            Spacer(Modifier.height(BottariTheme.spacing.space2xSmall))
            Text(
                text = it,
                color = if (isError) BottariTheme.colors.red else BottariTheme.colors.gray500,
                style = BottariTheme.typography.medium12.toTextStyle(),
            )
        }
    }
}

@ComponentPreview
@Composable
private fun BottariTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    BottariTheme {
        Box(modifier = Modifier.padding(12.dp)) {
            BottariTextField(
                value = text,
                onValueChange = { text = it },
                label = "닉네임",
                placeholder = "이름을 입력하세요",
                supportingText = "10자 이내",
            )
        }
    }
}
