package com.bottari.presentation.compose.edit.team.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bottari.presentation.R
import com.bottari.presentation.compose.common.component.BottariBox
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.BottariItemUiModel
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel

@Composable
fun TeamChecklistItem(
    item: BottariItemUiModel,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    BottariBox(
        modifier = modifier.fillMaxWidth(),
        contentPadding =
            PaddingValues(
                vertical = BottariTheme.spacing.spaceXSmall,
            ),
    ) {
        Text(
            text = item.name,
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = BottariTheme.spacing.space2xLarge),
            maxLines = 1,
            style = BottariTheme.typography.medium16.toTextStyle(),
        )
        IconButton(
            onClick = { onDeleteClick(item.id) },
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = BottariTheme.spacing.spaceMedium),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_delete),
                contentDescription = stringResource(R.string.common_btn_item_delete_description),
                modifier = Modifier.padding(8.dp),
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
