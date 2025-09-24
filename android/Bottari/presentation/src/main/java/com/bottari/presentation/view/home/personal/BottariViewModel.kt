package com.bottari.presentation.view.home.personal

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.di.usecase.CommonUseCaseProvider
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.notification.DeleteNotificationUseCase
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
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
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
                        copy(
                            isLoading = false,
                            bottaries = currentState.bottaries.filterNot { bottari -> bottari.id == bottariId },
                        )
                    }
                    deleteNotification(bottari)
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

    private fun deleteNotification(bottari: BottariUiModel?) {
        if (bottari == null) return
        launch {
            deleteNotificationUseCase(bottari.id)
                .onFailure { exception ->
                    BottariLogger.error(
                        exception.stackTraceToString(),
                        exception,
                    )
                }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    BottariViewModel(
                        BottariUseCaseProvider.fetchBottariesUseCase,
                        BottariUseCaseProvider.deleteBottariUseCase,
                        CommonUseCaseProvider.deleteNotificationUseCase,
                    )
                }
            }
    }
}
