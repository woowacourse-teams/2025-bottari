package com.bottari.presentation.view.create

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.model.bottari.BottariType
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottariCreateViewModel @Inject constructor(
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
                }.onFailure { emitEvent(BottariCreateUiEvent.CreateBottariFailure) }
        }
    }

    private fun createTeamBottari() {
        val title = currentState.bottariTitle.trim()

        launch {
            createTeamBottariUseCase(title)
                .onSuccess { createdBottariId ->
                    if (createdBottariId == null) return@onSuccess
                    logCreateBottariEvent(UiEventType.TEAM_BOTTARI_CREATE, title)
                    emitEvent(BottariCreateUiEvent.CreateTeamBottariSuccess(createdBottariId))
                }.onFailure { emitEvent(BottariCreateUiEvent.CreateBottariFailure) }
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
        const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        const val KEY_BOTTARI_TYPE = "KEY_BOTTARI_TYPE"
        private const val EMPTY_BOTTARI_TITLE = ""
        private const val ERROR_BOTTARI_TYPE = "[ERROR] 보따리 타입을 찾을 수 없습니다"
    }
}
