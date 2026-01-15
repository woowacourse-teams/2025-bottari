package com.bottari.feature.team.edit.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.bottari.designsystem.component.BottariCard
import com.bottari.bottari.designsystem.component.BottariIconButton
import com.bottari.bottari.designsystem.theme.BottariTheme
import com.bottari.core.ui.model.bottari.BottariItemUiModel
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.R as UIR

@Composable
fun TeamChecklistItem(
    item: BottariItemUiModel,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding =
            PaddingValues(
                top = BottariTheme.spacing.space2xSmall,
                bottom = BottariTheme.spacing.space2xSmall,
                start = BottariTheme.spacing.spaceMedium,
            ),
    ) {
        TeamChecklistItemContent(
            item = item,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
private fun TeamChecklistItemContent(
    item: BottariItemUiModel,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.name,
            maxLines = 1,
            style = BottariTheme.typography.medium16.toTextStyle(),
            modifier = Modifier.weight(1f),
        )
        BottariIconButton(onClick = { onDeleteClick(item.id) }) {
            Icon(
                painter = painterResource(UIR.drawable.ic_delete),
                contentDescription = stringResource(UIR.string.common_btn_item_delete_description),
                tint = BottariTheme.colors.gray600,
            )
        }
    }
}

@Composable
@Preview
private fun TeamChecklistItemPreview() {
    TeamChecklistItem(
        item =
            BottariItemUiModel(
                id = 1,
                name = "물건",
                type = BottariItemTypeUiModel.PERSONAL,
            ),
        onDeleteClick = {},
    )
}
