package com.bottari.presentation.view.home.personal

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.di.usecase.CommonUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.notification.DeleteNotificationUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel

class BottariViewModel(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase,
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
                        copy(bottaries = bottaries.map(BottariUiModel::fromDomain))
                    }
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.NotFoundException -> emitEvent(BottariUiEvent.FetchBottariesFailure.NotFoundException)
                        else -> emitEvent(BottariUiEvent.FetchBottariesFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(BottariUiEvent.FetchBottariesFailure.UnexpectedException) }

            updateState { copy(isLoading = false, isFetched = true) }
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
                    deleteNotification(bottari)
                    fetchBottaries()
                    emitEvent(BottariUiEvent.BottariDeleteSuccess)
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.NotFoundException -> emitEvent(BottariUiEvent.BottariDeleteFailure.NotFoundException)
                        is BottariException.PermissionException -> emitEvent(BottariUiEvent.BottariDeleteFailure.PermissionException)
                        else -> emitEvent(BottariUiEvent.BottariDeleteFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(BottariUiEvent.BottariDeleteFailure.UnexpectedException) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun deleteNotification(bottari: BottariUiModel?) {
        if (bottari == null) return
        launch {
            deleteNotificationUseCase(bottari.id)
                .onApiError { emitEvent(BottariUiEvent.DeleteNotificationFailure) }
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
