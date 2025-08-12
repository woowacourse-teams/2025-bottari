package com.bottari.presentation.view.edit.team.management

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.team.TeamMembers
import com.bottari.domain.usecase.team.FetchTeamMembersUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel

class TeamManagementViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamMembersUseCase: FetchTeamMembersUseCase,
) : BaseViewModel<TeamManagementUiState, TeamManagementUiEvent>(
        TeamManagementUiState(),
    ) {
    private val teamBottariId: Long =
        stateHandle[KEY_TEAM_BOTTARI_ID] ?: error(ERROR_REQUIRE_TEAM_BOTTARI_ID)

    fun fetchTeamMembers() {
        updateState { copy(isLoading = true) }
        launch {
            fetchTeamMembersUseCase(teamBottariId)
                .onSuccess { teamMembers ->
                    updateState { copyFromTeamMembers(teamMembers) }
                    BottariLogger.ui(
                        UiEventType.TEAM_BOTTARI_MEMBERS_FETCH,
                        mapOf(
                            "invite_code" to teamMembers.inviteCode,
                            "member_head_count" to teamMembers.teamMemberHeadCount.value,
                            "host_name" to teamMembers.hostName.value,
                            "members" to teamMembers.memberNicknames.map { nickname -> nickname.value },
                        ),
                    )
                }.onFailure {
                    emitEvent(TeamManagementUiEvent.FetchTeamMembersFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    private fun TeamManagementUiState.copyFromTeamMembers(teamMembers: TeamMembers): TeamManagementUiState =
        copy(
            inviteCode = teamMembers.inviteCode,
            teamMemberHeadCount = teamMembers.teamMemberHeadCount.value,
            maxHeadCount = teamMembers.teamMemberHeadCount.maxValue,
            members = teamMembers.toUiModel(),
        )

    companion object {
        private const val KEY_TEAM_BOTTARI_ID = "KEY_TEAM_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEAM_BOTTARI_ID = "[ERROR] 팀 보따리 ID가 존재하지 않습니다."

        fun Factory(teamBottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_TEAM_BOTTARI_ID] = teamBottariId
                    TeamManagementViewModel(
                        stateHandle = stateHandle,
                        fetchTeamMembersUseCase = UseCaseProvider.fetchTeamMembersUseCase,
                    )
                }
            }
    }
}
