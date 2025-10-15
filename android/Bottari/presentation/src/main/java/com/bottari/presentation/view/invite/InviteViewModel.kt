package com.bottari.presentation.view.invite

import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
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
}
