package com.bottari.feature.personal.edit.item.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.R
import com.bottari.core.ui.extension.topBottomFadingEdge
import com.bottari.core.ui.model.bottari.PersonalChecklistItemUiModel
import com.bottari.core.ui.model.bottari.team.ChecklistItemUiModel
import com.bottari.core.ui.R as PresentationR

@Composable
fun ItemEditLazyColumn(
    items: List<PersonalChecklistItemUiModel>,
    onDeleteClick: (Long) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .padding(horizontal = BottariTheme.spacing.spaceLarge)
                .topBottomFadingEdge(color = BottariTheme.colors.gray50, width = 8.dp),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(BottariTheme.spacing.spaceXSmall),
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(
            items = items,
            key = { item -> item.id },
        ) { item ->
            BottariItem(
                item = item,
                onDeleteClick = onDeleteClick,
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@Composable
private fun BottariItem(
    item: ChecklistItemUiModel,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = BottariTheme.spacing.space2xSmall),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.name,
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            horizontal = BottariTheme.spacing.spaceMedium,
                        ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = BottariTheme.typography.semiBold16.toTextStyle(),
            )
            BottariIconButton(onClick = { onDeleteClick(item.id) }) {
                Icon(
                    painter = painterResource(PresentationR.drawable.ic_delete),
                    contentDescription = stringResource(R.string.common_btn_item_delete_description),
                    modifier = Modifier.padding(BottariTheme.spacing.spaceXSmall),
                    tint = BottariTheme.colors.gray600,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEditLazyColumnPreview() {
    BottariTheme {
        ItemEditLazyColumn(
            items =
                listOf(
                    PersonalChecklistItemUiModel(
                        id = 1L,
                        name = "하나하나하나하나하나하나하나하나하나하ㅎ하",
                        isChecked = false,
                    ),
                ),
            onDeleteClick = {},
            listState = rememberLazyListState(),
        )
    }
}
