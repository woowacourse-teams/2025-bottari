package com.bottari.presentation.view.join

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.TeamMemberUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
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
                .onApiError { emitEvent(TeamBottariJoinUiEvent.UnexpectedException) }
                .onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.NotFoundException ->
                            emitEvent(TeamBottariJoinUiEvent.JoinTeamBottariFailure.NotFoundException)

                        is BottariException.DuplicatedException ->
                            emitEvent(TeamBottariJoinUiEvent.JoinTeamBottariFailure.DuplicatedException)

                        else -> emitEvent(TeamBottariJoinUiEvent.UnexpectedException)
                    }
                }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TeamBottariJoinViewModel(
                        TeamMemberUseCaseProvider.joinTeamBottariUseCase,
                    )
                }
            }
    }
}
