package com.bottari.presentation.view.home.bottari.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.presentation.common.base.BaseViewModel
import kotlinx.coroutines.launch

class BottariCreateViewModel(
    stateHandle: SavedStateHandle,
    private val createBottariUseCase: CreateBottariUseCase,
) : BaseViewModel<BottariCreateUiState, BottariCreateUiEvent>(
        BottariCreateUiState(
            stateHandle[KEY_BOTTARI_TITLE] ?: EMPTY_BOTTARI_TITLE,
        ),
    ) {
    private val ssaid: String = stateHandle[KEY_SSAID]!!

    fun updateBottariTitle(title: String) {
        updateState { copy(bottariTitle = title) }
    }

    fun createBottari() {
        val title = uiState.value?.bottariTitle?.trim() ?: return
        if (title.isBlank()) return

        launch {
            createBottariUseCase(ssaid, title)
                .onSuccess { emitEvent(BottariCreateUiEvent.CreateBottariSuccess(it)) }
                .onFailure { emitEvent(BottariCreateUiEvent.CreateBottariFailure) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_BOTTARI_TITLE = "KEY_BOTTARI_TITLE"
        private const val EMPTY_BOTTARI_TITLE = ""

        fun Factory(
            ssaid: String,
            defaultTitle: String,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
                    stateHandle[KEY_BOTTARI_TITLE] = defaultTitle

                    BottariCreateViewModel(
                        stateHandle,
                        UseCaseProvider.createBottariUseCase,
                    )
                }
            }
    }
}
