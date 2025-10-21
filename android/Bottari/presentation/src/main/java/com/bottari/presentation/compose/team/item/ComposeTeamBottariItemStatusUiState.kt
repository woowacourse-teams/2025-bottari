package com.bottari.presentation.compose.team.item

import com.bottari.presentation.model.bottari.team.TeamBottariUiModelStatus

data class ComposeTeamBottariItemStatusUiState(
    val isLoading: Boolean = false,
    val items: List<TeamBottariUiModelStatus> = listOf(),
    val selectedProduct: TeamBottariUiModelStatus? = null,
) {
    private val totalCount =
        items.sumOf { it.totalItemsCount }
    private val checkedCount =
        items.sumOf { it.checkItemsCount }

    val completedItems =
        items.count { it.checkItemsCount == it.totalItemsCount }
    val checkedProgress = ((checkedCount.toFloat() / totalCount.toFloat()) * 100).toInt()
}
