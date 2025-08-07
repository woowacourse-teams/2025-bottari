package com.bottari.presentation.view.home.bottari

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel

class BottariViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
) : BaseViewModel<BottariUiState, BottariUiEvent>(BottariUiState()) {
    private val ssaid: String = stateHandle.get<String>(KEY_SSAID)!!

    init {
        fetchBottaries()
    }

    fun fetchBottaries() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariesUseCase(ssaid)
                .onSuccess { bottaries ->
                    updateState {
                        copy(bottaries = bottaries.map { bottari -> bottari.toUiModel() })
                    }
                }.onFailure {
                    emitEvent(BottariUiEvent.FetchBottariesFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun deleteBottari(bottariId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteBottariUseCase(ssaid, bottariId)
                .onSuccess {
                    fetchBottaries()
                    emitEvent(BottariUiEvent.BottariDeleteSuccess)
                }.onFailure {
                    emitEvent(BottariUiEvent.BottariDeleteFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    BottariViewModel(
                        stateHandle,
                        UseCaseProvider.fetchBottariesUseCase,
                        UseCaseProvider.deleteBottariUseCase,
                    )
                }
            }
    }
}
