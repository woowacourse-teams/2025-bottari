package com.bottari.presentation.compose.team.checklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bottari.presentation.compose.common.component.IndeterminateCircularIndicator
import com.bottari.presentation.compose.common.theme.BottariTheme
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.presentation.model.bottari.team.TeamChecklistItemUiModel
import kotlin.random.Random

@Composable
fun TeamBottariChecklistScreen(
    uiState: ComposeTeamChecklistUiState,
    isTooltipClose: Boolean,
    onClickSection: (BottariItemTypeUiModel) -> Unit,
    onCloseToolTip: () -> Unit,
    onToggleItem: (Long, BottariItemTypeUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(BottariTheme.spacing.spaceMedium),
        verticalArrangement = Arrangement.Top,
    ) {
        if (uiState.isLoading) {
            IndeterminateCircularIndicator()
            return
        }

        TeamChecklistScreen(
            uiState = uiState,
            isToolTipClosed = isTooltipClose,
            onCloseToolTip = onCloseToolTip,
            onClickSection = { type -> onClickSection(type) },
            onClickItem = onToggleItem,
        )
    }
}

@Preview
@Composable
private fun TeamBottariChecklistScreenPreview() {
    TeamBottariChecklistScreen(
        isTooltipClose = false,
        uiState = dummyUiState,
        onClickSection = {},
        onCloseToolTip = {},
        onToggleItem = { _, _ -> },
    )
}

private fun createDummyProductList(
    count: Int,
    type: BottariItemTypeUiModel,
    idStartIndex: Long = 0,
): List<TeamChecklistItemUiModel> =
    List(count) { index ->
        TeamChecklistItemUiModel(
            id = idStartIndex + index,
            name = "더미 아이템 ${idStartIndex + index + 1}",
            isChecked = Random.nextBoolean(),
            type = type,
        )
    }

val dummyUiState =
    ComposeTeamChecklistUiState(
        isLoading = false,
        bottariItems =
            createDummyProductList(
                10,
                BottariItemTypeUiModel.SHARED,
            ) + createDummyProductList(10, BottariItemTypeUiModel.PERSONAL) +
                createDummyProductList(10, BottariItemTypeUiModel.ASSIGNED()),
        sections =
            mapOf(
                BottariItemTypeUiModel.SHARED to true,
                BottariItemTypeUiModel.ASSIGNED() to true,
                BottariItemTypeUiModel.PERSONAL to true,
            ),
    )
