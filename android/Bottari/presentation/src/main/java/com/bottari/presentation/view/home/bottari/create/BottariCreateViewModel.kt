package com.bottari.presentation.view.home.bottari.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.update
import kotlinx.coroutines.launch

class BottariCreateViewModel(
    stateHandle: SavedStateHandle,
    private val createBottariUseCase: CreateBottariUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<BottariCreateUiState> =
        MutableLiveData(
            BottariCreateUiState(stateHandle[KEY_BOTTARI_TITLE] ?: EMPTY_BOTTARI_TITLE),
        )
    val uiState: LiveData<BottariCreateUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<BottariCreateUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<BottariCreateUiEvent> get() = _uiEvent

    fun updateBottariTitle(title: String) {
        _uiState.update { copy(bottariTitle = title) }
    }

    fun createBottari(
        ssaid: String,
        title: String,
    ) {
        if (title.isBlank()) return

        viewModelScope.launch {
            createBottariUseCase(ssaid, title)
                .onSuccess { _uiEvent.value = BottariCreateUiEvent.CreateBottariSuccess(it) }
                .onFailure { _uiEvent.value = BottariCreateUiEvent.CreateBottariFailure }
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
