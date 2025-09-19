package com.bottari.presentation.view.home.team

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.TeamBottariUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.team.ExitTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel

class TeamBottariViewModel(
    private val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase,
    private val exitTeamBottariUseCase: ExitTeamBottariUseCase,
) : BaseViewModel<TeamBottariUiState, TeamBottariUiEvent>(TeamBottariUiState()) {
    fun fetchBottaries() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamBottariesUseCase()
                .onSuccess { bottaries ->
                    val newBottaries =
                        bottaries.map { bottari -> TeamBottariUiModel.fromDomain(bottari) }
                    updateState { copy(bottaries = newBottaries) }
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.NotFoundException -> emitEvent(TeamBottariUiEvent.FetchBottariesFailure.NotFoundException)
                        else -> emitEvent(TeamBottariUiEvent.FetchBottariesFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(TeamBottariUiEvent.FetchBottariesFailure.UnexpectedException)
                }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    fun deleteBottari(bottariId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            exitTeamBottariUseCase(bottariId)
                .onSuccess {
                    logPersonalBottariDelete(bottariId)
                    fetchBottaries()
                    emitEvent(TeamBottariUiEvent.BottariDeleteSuccess)
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.PermissionException -> emitEvent(TeamBottariUiEvent.BottariDeleteFailure.PermissionException)
                        is BottariException.NotFoundException -> emitEvent(TeamBottariUiEvent.BottariDeleteFailure.NotFoundException)
                        else -> emitEvent(TeamBottariUiEvent.BottariDeleteFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(TeamBottariUiEvent.BottariDeleteFailure.UnexpectedException)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun logPersonalBottariDelete(bottariId: Long) {
        val bottari = currentState.bottaries.find { it.id == bottariId }
        BottariLogger.ui(
            UiEventType.TEAM_BOTTARI_EXIT,
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
                        TeamBottariUseCaseProvider.fetchTeamBottariesUseCase,
                        TeamBottariUseCaseProvider.exitTeamBottariUseCase,
                    )
                }
            }
    }
}
