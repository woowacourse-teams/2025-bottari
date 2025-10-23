package com.bottari.presentation.compose.home.bottari

import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel

data class MyBottariUiState(
    val isLoading: Boolean = false,
    val isPersonalFetched: Boolean = false,
    val isTeamFetched: Boolean = false,
    val personalBottaries: List<BottariUiModel> = emptyList(),
    val teamBottaries: List<TeamBottariUiModel> = emptyList(),
    val showDialogType: MyBottariDialogType? = null,
) {
    val isFetched: Boolean = isPersonalFetched && isTeamFetched
    val isPersonalEmpty: Boolean = isFetched && personalBottaries.isEmpty()
    val isTeamEmpty: Boolean = isFetched && teamBottaries.isEmpty()
    val allBottaries: List<MyBottariUiModel> = teamBottaries + personalBottaries
    val isAllEmpty: Boolean = isFetched && allBottaries.isEmpty()
}
