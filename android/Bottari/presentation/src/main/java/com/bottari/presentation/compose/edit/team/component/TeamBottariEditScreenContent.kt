package com.bottari.presentation.compose.edit.team.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.compose.common.modifier.noRippleClickable
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.compose.edit.personal.item.component.ItemEditInputBar
import com.bottari.presentation.model.bottari.BottariItemUiModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

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
        if (items.isEmpty()) {
            TeamEditEmptyView(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            )
        } else {
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

@Composable
@Preview(showBackground = true)
private fun TeamChecklistEditScreenPreview() {
    val items =
        listOf(
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
            BottariItemUiModel(1, "물건", BottariItemTypeUiModel.PERSONAL),
        )

    TeamChecklistEditScreen(
        items = items,
        onDeleteItem = {},
        newItemName = "",
        isInvalidItem = false,
        isSavable = true,
        onChangeItemName = {},
        onSaveItem = {},
    )
}
