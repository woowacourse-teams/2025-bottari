package com.bottari.presentation.view.home.team

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel

class TeamBottariViewModel(
    private val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
) : BaseViewModel<TeamBottariUiState, TeamBottariUiEvent>(TeamBottariUiState()) {
    fun fetchBottaries() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamBottariesUseCase()
                .onSuccess { bottaries ->
                    updateState {
                        copy(bottaries = bottaries.map { bottari -> bottari.toUiModel() })
                    }
                }.onFailure { emitEvent(TeamBottariUiEvent.FetchBottariesFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    fun deleteBottari(bottariId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteBottariUseCase(bottariId)
                .onSuccess {
                    logPersonalBottariDelete(bottariId)
                    fetchBottaries()
                    emitEvent(TeamBottariUiEvent.BottariDeleteSuccess)
                }.onFailure { emitEvent(TeamBottariUiEvent.BottariDeleteFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun logPersonalBottariDelete(bottariId: Long) {
        val bottari = currentState.bottaries.find { it.id == bottariId }
        BottariLogger.ui(
            UiEventType.PERSONAL_BOTTARI_DELETE,
            mapOf(
                "bottari_id" to bottariId,
                "bottari_title" to bottari?.title.orEmpty(),
            ),
        )
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TeamBottariViewModel(
                        UseCaseProvider.fetchTeamBottariesUseCase,
                        UseCaseProvider.deleteBottariUseCase,
                    )
                }
            }
    }
}
