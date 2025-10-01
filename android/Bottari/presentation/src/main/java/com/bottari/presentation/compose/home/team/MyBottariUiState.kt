package com.bottari.presentation.compose.home.team

import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel

data class MyBottariUiState(
    val isLoading: Boolean = false,
    val personalBottaries: List<BottariUiModel> = emptyList(),
    val teamBottaries: List<TeamBottariUiModel> = emptyList(),
    val showDialogType: MyBottariDialogType? = null,
) {
    val myBottaries: List<MyBottariUiModel> = personalBottaries + teamBottaries
}
