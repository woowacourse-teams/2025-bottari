package com.bottari.presentation.view.home.personal

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class BottariViewModel(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
) : FlowBaseViewModel<BottariUiState, BottariUiEvent>(BottariUiState()) {
    init {
        fetchBottaries()
    }

    fun deleteBottari(bottariId: Long) {
        updateState { copy(isLoading = true) }
        launch {
            deleteBottariUseCase(bottariId)
                .onSuccess {
                    val bottari = currentState.bottaries.find { it.id == bottariId }
                    BottariLogger.ui(
                        UiEventType.PERSONAL_BOTTARI_DELETE,
                        mapOf(
                            "bottari_id" to bottariId,
                            "bottari_title" to bottari?.title.orEmpty(),
                        ),
                    )
                    updateState {
                        copy(bottaries = currentState.bottaries.filterNot { bottari -> bottari.id == bottariId })
                    }
                    emitEvent(BottariUiEvent.BottariDeleteSuccess)
                }.onFailure {
                    emitEvent(BottariUiEvent.BottariDeleteFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchBottaries() {
        updateState { copy(isLoading = true) }
        fetchBottariesUseCase()
            .onEach { bottaries ->
                updateState {
                    copy(
                        isLoading = false,
                        bottaries = bottaries.map(BottariUiModel::fromPersonalBottari),
                        isFetched = true,
                    )
                }
            }.catch {
                emitEvent(BottariUiEvent.FetchBottariesFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    BottariViewModel(
                        BottariUseCaseProvider.fetchBottariesUseCase,
                        BottariUseCaseProvider.deleteBottariUseCase,
                    )
                }
            }
    }
}
