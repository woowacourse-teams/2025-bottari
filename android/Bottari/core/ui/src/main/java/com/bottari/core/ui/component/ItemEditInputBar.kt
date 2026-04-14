package com.bottari.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.component.BottariTextField
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R

@Composable
fun ItemEditInputBar(
    itemName: String,
    isInvalidItem: Boolean,
    isSavable: Boolean,
    onNameChange: (String) -> Unit,
    onSaveItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        BottariTextField(
            value = itemName,
            onValueChange = onNameChange,
            isError = isInvalidItem,
            supportingText = if (isInvalidItem) stringResource(R.string.bottari_item_edit_supporting_text) else null,
            placeholder = stringResource(R.string.bottari_item_edit_hint_text),
            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = BottariTheme.spacing.spaceSmall),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onSaveItem() }),
        )

        BottariIconButton(
            onClick = onSaveItem,
            enabled = isSavable,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.bottari_btn_item_add_description),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEditInputBarPreview() {
    BottariTheme {
        ItemEditInputBar(
            itemName = "",
            isInvalidItem = false,
            isSavable = false,
            onNameChange = {},
            onSaveItem = {},
        )
    }
}
