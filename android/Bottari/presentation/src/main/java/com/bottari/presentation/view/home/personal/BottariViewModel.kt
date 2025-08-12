package com.bottari.presentation.view.home.personal

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel

class BottariViewModel(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
) : BaseViewModel<BottariUiState, BottariUiEvent>(BottariUiState()) {
    init {
        fetchBottaries()
    }

    fun fetchBottaries() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariesUseCase()
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
                    fetchBottaries()
                    emitEvent(BottariUiEvent.BottariDeleteSuccess)
                }.onFailure {
                    emitEvent(BottariUiEvent.BottariDeleteFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    BottariViewModel(
                        UseCaseProvider.fetchBottariesUseCase,
                        UseCaseProvider.deleteBottariUseCase,
                    )
                }
            }
    }
}
