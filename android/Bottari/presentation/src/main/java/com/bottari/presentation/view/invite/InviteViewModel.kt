package com.bottari.presentation.view.invite

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
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
                .onFailure { emitEvent(InviteUiEvent.JoinTeamBottariFailure) }
            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    InviteViewModel(UseCaseProvider.joinTeamBottariUseCase)
                }
            }
    }
}
