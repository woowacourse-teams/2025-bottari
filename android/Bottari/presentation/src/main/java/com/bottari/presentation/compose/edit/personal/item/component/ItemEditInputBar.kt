package com.bottari.presentation.compose.edit.personal.item.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.theme.BottariTheme

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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ItemTextField(
            itemName = itemName,
            onNameChange = onNameChange,
            onSaveItem = onSaveItem,
            isError = isInvalidItem,
            modifier =
                Modifier
                    .height(48.dp)
                    .weight(1f)
                    .padding(start = BottariTheme.spacing.spaceSmall),
        )
        IconButton(
            onClick = onSaveItem,
            enabled = isSavable,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.bottari_btn_item_add_description),
            )
        }
    }
}