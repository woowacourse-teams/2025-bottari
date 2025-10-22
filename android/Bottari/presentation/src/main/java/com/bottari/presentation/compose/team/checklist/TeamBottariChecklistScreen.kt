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

@Composable
fun TeamBottariChecklistScreen(
    uiState: ComposeTeamChecklistUiState,
    isTooltipClose: Boolean,
    onClickSection: (BottariItemTypeUiModel) -> Unit,
    onCloseToolTip: () -> Unit,
    onToggleItem: (Long, BottariItemTypeUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.isLoading) {
        IndeterminateCircularIndicator()
        return
    }
    Column(
        modifier = modifier.fillMaxSize().padding(BottariTheme.spacing.spaceMedium),
        verticalArrangement = Arrangement.Top,
    ) {
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
        uiState = teamChecklistDummyUiState,
        onClickSection = {},
        onCloseToolTip = {},
        onToggleItem = { _, _ -> },
    )
}
