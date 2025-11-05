package com.bottari.presentation.compose.edit.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.personal.item.component.ItemEditInputBar
import com.bottari.presentation.model.bottari.BottariItemUiModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamChecklistEditScreen(
    items: List<BottariItemUiModel>,
    newItemName: String,
    isInvalidItem: Boolean,
    isSavable: Boolean,
    onDeleteItem: (Long) -> Unit,
    onChangeItemName: (String) -> Unit,
    onSaveItem: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.noRippleClickable(onClick = focusManager::clearFocus),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceSmall),
            contentPadding =
                PaddingValues(bottom = BottariTheme.spacing.spaceMedium),
            modifier =
                Modifier
                    .padding(horizontal = BottariTheme.spacing.spaceMedium)
                    .padding(top = BottariTheme.spacing.spaceSmall)
                    .weight(1f),
        ) {
            items(items) { item ->
                TeamChecklistItem(item = item, onDeleteClick = onDeleteItem)
            }
        }
        ItemEditInputBar(
            itemName = newItemName,
            isInvalidItem = isInvalidItem,
            isSavable = isSavable,
            onNameChange = onChangeItemName,
            onSaveItem = onSaveItem,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        )
    }
}
