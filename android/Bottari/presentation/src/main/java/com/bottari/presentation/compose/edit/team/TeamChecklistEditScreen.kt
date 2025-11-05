package com.bottari.presentation.compose.edit.team

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
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
        if (items.isEmpty()) {
            TeamChecklistEmptyView(modifier = Modifier.fillMaxWidth().weight(1f))
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
private fun TeamChecklistEmptyView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_bottari_item_empty_view),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = BottariTheme.colors.gray500,
        )
        Spacer(Modifier.height(BottariTheme.spacing.spaceSmall))
        Text(
            text = "해당하는 물건이 없어요",
            color = BottariTheme.colors.gray500,
            style = BottariTheme.typography.bold20.toTextStyle(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamChecklistEmptyViewPreview() {
    TeamChecklistEmptyView(Modifier.fillMaxSize())
}
