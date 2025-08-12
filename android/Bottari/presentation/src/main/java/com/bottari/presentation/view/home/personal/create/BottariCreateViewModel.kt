package com.bottari.presentation.view.home.personal.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel

class BottariCreateViewModel(
    stateHandle: SavedStateHandle,
    private val createBottariUseCase: CreateBottariUseCase,
) : BaseViewModel<BottariCreateUiState, BottariCreateUiEvent>(
        BottariCreateUiState(
            stateHandle[KEY_BOTTARI_TITLE] ?: EMPTY_BOTTARI_TITLE,
        ),
    ) {
    fun updateBottariTitle(title: String) {
        updateState { copy(bottariTitle = title) }
    }

    fun createBottari() {
        val title = uiState.value?.bottariTitle?.trim() ?: return
        if (title.isBlank()) return

        launch {
            createBottariUseCase(title)
                .onSuccess { createdBottariId ->
                    if (createdBottariId == null) return@onSuccess
                    BottariLogger.ui(
                        UiEventType.PERSONAL_BOTTARI_CREATE,
                        mapOf("bottari_id" to createdBottariId, "bottari_title" to title),
                    )
                    emitEvent(BottariCreateUiEvent.CreateBottariSuccess(createdBottariId))
                }.onFailure { emitEvent(BottariCreateUiEvent.CreateBottariFailure) }
        }
    }

    companion object {
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val EMPTY_BOTTARI_TITLE = ""

        fun Factory(defaultTitle: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_TITLE] = defaultTitle

                    BottariCreateViewModel(
                        stateHandle,
                        UseCaseProvider.createBottariUseCase,
                    )
                }
            }
    }
}
