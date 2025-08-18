package com.bottari.presentation.view.join

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel

class TeamBottariJoinViewModel(
    private val joinTeamBottariUseCase: JoinTeamBottariUseCase,
) : BaseViewModel<TeamBottariJoinUiState, TeamBottariJoinUiEvent>(TeamBottariJoinUiState()) {
    fun updateInviteCode(inviteCode: String) {
        if (currentState.inviteCode == inviteCode) return
        updateState { copy(inviteCode = inviteCode) }
    }

    fun joinTeamBottari() {
        if (currentState.isCanJoin.not()) return

        launch {
            joinTeamBottariUseCase(currentState.inviteCode)
                .onSuccess { emitEvent(TeamBottariJoinUiEvent.JoinTeamBottariSuccess) }
                .onFailure { emitEvent(TeamBottariJoinUiEvent.JoinTeamBottariFailure) }
        }
    }

    private fun logJoinTeamBottariEvent(joinedTeamBottariId: Long?) {
        BottariLogger.ui(
            UiEventType.TEAM_BOTTARI_JOIN,
            mapOf("bottari_id" to (joinedTeamBottariId ?: NOT_FOUND_JOINED_TEAM_BOTTARI_ID)),
        )
    }

    companion object {
        private const val NOT_FOUND_JOINED_TEAM_BOTTARI_ID = -1

        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TeamBottariJoinViewModel(
                        UseCaseProvider.joinTeamBottariUseCase,
                    )
                }
            }
    }
}
