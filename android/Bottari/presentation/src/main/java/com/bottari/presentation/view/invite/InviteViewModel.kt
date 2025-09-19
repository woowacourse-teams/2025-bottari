package com.bottari.presentation.view.invite

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

class InviteViewModel(
    private val joinTeamBottariUseCase: JoinTeamBottariUseCase,
) : BaseViewModel<InviteUiState, InviteUiEvent>(InviteUiState()) {
    fun joinTeamBottari(inviteCode: String) {
        updateState { copy(isLoading = true) }
        launch {
            joinTeamBottariUseCase(inviteCode)
                .onSuccess { emitEvent(InviteUiEvent.JoinTeamBottariSuccess) }
                .onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.NotFoundException -> emitEvent(InviteUiEvent.JoinTeamBottariFailure.NotFoundException)
                        BottariException.DuplicatedException -> emitEvent(InviteUiEvent.JoinTeamBottariFailure.DuplicatedException)
                        else -> emitEvent(InviteUiEvent.JoinTeamBottariFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(InviteUiEvent.JoinTeamBottariFailure.UnexpectedException) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    InviteViewModel(TeamMemberUseCaseProvider.joinTeamBottariUseCase)
                }
            }
    }
}
