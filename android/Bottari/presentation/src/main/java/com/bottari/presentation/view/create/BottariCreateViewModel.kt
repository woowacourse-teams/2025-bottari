package com.bottari.presentation.view.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.di.usecase.TeamBottariUseCaseProvider
import com.bottari.domain.model.bottari.BottariType
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel

class BottariCreateViewModel(
    stateHandle: SavedStateHandle,
    private val createBottariUseCase: CreateBottariUseCase,
    private val createTeamBottariUseCase: CreateTeamBottariUseCase,
) : BaseViewModel<BottariCreateUiState, BottariCreateUiEvent>(
        BottariCreateUiState(
            BottariType.valueOf(stateHandle[KEY_BOTTARI_TYPE] ?: error(ERROR_BOTTARI_TYPE)),
            stateHandle[KEY_BOTTARI_TITLE] ?: EMPTY_BOTTARI_TITLE,
        ),
    ) {
    fun updateBottariTitle(title: String) {
        updateState { copy(bottariTitle = title) }
    }

    fun createBottari() {
        if (currentState.isCanCreate.not()) return

        when (currentState.bottariType) {
            BottariType.PERSONAL -> createPersonalBottari()
            BottariType.TEAM -> createTeamBottari()
        }
    }

    private fun createPersonalBottari() {
        val title = currentState.bottariTitle.trim()

        launch {
            createBottariUseCase(title)
                .onSuccess { createdBottariId ->
                    logCreateBottariEvent(UiEventType.PERSONAL_BOTTARI_CREATE, title)
                    emitEvent(BottariCreateUiEvent.CreatePersonalBottariSuccess(createdBottariId))
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.InvalidException ->
                            emitEvent(BottariCreateUiEvent.CreateBottariFailure.InvalidException)

                        BottariException.NotFoundException ->
                            emitEvent(BottariCreateUiEvent.CreateBottariFailure.NotFoundException)

                        else -> emitEvent(BottariCreateUiEvent.CreateBottariFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(BottariCreateUiEvent.CreateBottariFailure.UnexpectedException) }
        }
    }

    private fun createTeamBottari() {
        val title = currentState.bottariTitle.trim()

        launch {
            createTeamBottariUseCase(title)
                .onSuccess { createdBottariId ->
                    logCreateBottariEvent(UiEventType.TEAM_BOTTARI_CREATE, title)
                    emitEvent(BottariCreateUiEvent.CreateTeamBottariSuccess(createdBottariId))
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.InvalidException ->
                            emitEvent(BottariCreateUiEvent.CreateBottariFailure.InvalidException)

                        BottariException.NotFoundException ->
                            emitEvent(BottariCreateUiEvent.CreateBottariFailure.NotFoundException)

                        else -> emitEvent(BottariCreateUiEvent.CreateBottariFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(BottariCreateUiEvent.CreateBottariFailure.UnexpectedException) }
        }
    }

    private fun logCreateBottariEvent(
        type: UiEventType,
        title: String,
    ) {
        BottariLogger.ui(
            type,
            mapOf("bottari_type" to type, "bottari_title" to title),
        )
    }

    companion object {
        private const val EMPTY_BOTTARI_TITLE = ""
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val KEY_BOTTARI_TYPE = "KEY_BOTTARI_TYPE"
        private const val ERROR_BOTTARI_TYPE = "[ERROR] 보따리 타입을 찾을 수 없습니다"

        fun Factory(
            defaultTitle: String,
            type: String,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_TITLE] = defaultTitle
                    stateHandle[KEY_BOTTARI_TYPE] = type

                    BottariCreateViewModel(
                        stateHandle,
                        BottariUseCaseProvider.createBottariUseCase,
                        TeamBottariUseCaseProvider.createTeamBottariUseCase,
                    )
                }
            }
    }
}
