package com.bottari.feature.invite

import androidx.compose.runtime.Stable
import com.bottari.core.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.core.ui.base.FlowBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Stable
@HiltViewModel
class InviteViewModel @Inject constructor(
    private val joinTeamBottariUseCase: JoinTeamBottariUseCase,
) : FlowBaseViewModel<InviteUiState, InviteUiEvent>(InviteUiState()) {
    fun joinTeamBottari(inviteCode: String) {
        updateState { copy(isLoading = true) }
        launch {
            joinTeamBottariUseCase(inviteCode)
                .onSuccess { emitEvent(InviteUiEvent.JoinTeamBottariSuccess) }
                .onFailure { emitEvent(InviteUiEvent.JoinTeamBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }
}
