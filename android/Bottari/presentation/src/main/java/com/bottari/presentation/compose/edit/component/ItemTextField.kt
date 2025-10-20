package com.bottari.presentation.compose.edit.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

@Composable
fun ItemTextField(
    itemName: String,
    onNameChange: (String) -> Unit,
    onSaveItem: () -> Unit,
    isDuplicate: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = itemName,
        onValueChange = onNameChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = stringResource(R.string.bottari_personal_item_edit_hint_text),
                color = BottariTheme.colors.gray500,
                style = BottariTheme.typography.regular14.toTextStyle(),
            )
        },
        isError = isDuplicate,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = { onSaveItem() }),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BottariTheme.colors.gray200,
                unfocusedContainerColor = BottariTheme.colors.gray200,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                errorContainerColor = BottariTheme.colors.gray200,
                errorTextColor = BottariTheme.colors.red,
                errorCursorColor = BottariTheme.colors.red,
                errorBorderColor = BottariTheme.colors.red,
            ),
    )
}

@Preview
@Composable
private fun ItemTextFieldPreview() {
    var item by remember { mutableStateOf("") }

    BottariTheme {
        ItemTextField(
            itemName = item,
            onNameChange = { item = it },
            onSaveItem = {},
            isDuplicate = false,
        )
    }
}
