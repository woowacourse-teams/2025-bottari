package com.bottari.presentation.view.team.status

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.FetchTeamMembersStatusUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel

class TeamMembersStatusViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamMembersStatusUseCase: FetchTeamMembersStatusUseCase,
) : BaseViewModel<TeamMembersStatusUiState, TeamMembersStatusUiEvent>(
        TeamMembersStatusUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_TEAM_BOTTARI_ID] ?: error(ERROR_REQUIRE_TEAM_BOTTARI_ID)

    fun fetchTeamMembersStatus() {
        updateState { copy(isLoading = true) }
        launch {
            fetchTeamMembersStatusUseCase(teamBottariId)
                .onSuccess { membersStatus ->
                    updateState { copy(membersStatus = membersStatus.map { memberStatus -> memberStatus.toUiModel() }) }
                }.onFailure { emitEvent(TeamMembersStatusUiEvent.FetchMembersStatusFailure) }
            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        private const val KEY_TEAM_BOTTARI_ID = "KEY_TEAM_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEAM_BOTTARI_ID = "[ERROR] 팀 보따리 ID가 존재하지 않습니다."

        fun Factory(id: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_TEAM_BOTTARI_ID] = id
                    TeamMembersStatusViewModel(
                        stateHandle = stateHandle,
                        fetchTeamMembersStatusUseCase = UseCaseProvider.fetchTeamMembersStatusUseCase,
                    )
                }
            }
    }
}
