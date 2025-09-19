package com.bottari.presentation.view.template.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel

class TemplateDetailViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase,
    private val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase,
) : BaseViewModel<TemplateDetailUiState, TemplateDetailUiEvent>(
        TemplateDetailUiState(
            templateId = stateHandle[KEY_TEMPLATE_ID] ?: error(ERROR_REQUIRE_TEMPLATE_ID),
        ),
    ) {
    init {
        fetchBottariTemplateDetail()
    }

    fun takeBottariTemplate() {
        updateState { copy(isLoading = true) }

        launch {
            takeBottariTemplateDetailUseCase(currentState.templateId)
                .onSuccess { createdBottariId ->
                    logTakeBottariSuccess()
                    emitEvent(TemplateDetailUiEvent.TakeBottariTemplateSuccess(createdBottariId))
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.NotFoundException,
                        -> emitEvent(TemplateDetailUiEvent.TakeBottariTemplateFailure.NotFoundException)

                        is BottariException.InvalidException,
                        -> emitEvent(TemplateDetailUiEvent.TakeBottariTemplateFailure.InvalidException)

                        else -> emitEvent(TemplateDetailUiEvent.TakeBottariTemplateFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(TemplateDetailUiEvent.TakeBottariTemplateFailure.UnexpectedException)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchBottariTemplateDetail() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariTemplateDetailUseCase(currentState.templateId)
                .onSuccess { template ->
                    val itemUiModels =
                        template.items.map { BottariTemplateItemUiModel.fromDomain(it) }
                    updateState { copy(title = template.title, items = itemUiModels) }
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.NotFoundException,
                        -> emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure.NotFoundException)

                        else -> emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure.UnexpectedException)
                }
            updateState { copy(isLoading = false) }
        }
    }

    private fun logTakeBottariSuccess() {
        BottariLogger.ui(
            UiEventType.TEMPLATE_TAKE,
            mapOf(
                "template_id" to currentState.templateId,
                "template_title" to currentState.title,
                "template_items" to currentState.items.toString(),
            ),
        )
    }

    companion object {
        private const val KEY_TEMPLATE_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEMPLATE_ID = "[ERROR] 템플릿 ID가 존재하지 않습니다"

        fun Factory(templateId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val savedStateHandle = this.createSavedStateHandle()
                    savedStateHandle[KEY_TEMPLATE_ID] = templateId
                    TemplateDetailViewModel(
                        stateHandle = savedStateHandle,
                        fetchBottariTemplateDetailUseCase = BottariTemplateUseCaseProvider.fetchBottariTemplateDetailUseCase,
                        takeBottariTemplateDetailUseCase = BottariTemplateUseCaseProvider.takeBottariTemplateDetailUseCase,
                    )
                }
            }
    }
}
