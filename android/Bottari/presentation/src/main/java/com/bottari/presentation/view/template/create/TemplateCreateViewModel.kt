package com.bottari.presentation.view.template.create

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.SelectableBottariUiModel

class TemplateCreateViewModel(
    private val fetchBottariDetailsUseCase: FetchBottariDetailsUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : BaseViewModel<TemplateCreateUiState, TemplateCreateUiEvent>(TemplateCreateUiState()) {
    init {
        fetchBottariDetails()
    }

    fun changeBottari(bottariId: Long) {
        updateState {
            copy(
                selectedBottariId = bottariId,
                bottaries = bottaries.updateBottariSelectedState(bottariId),
            )
        }
    }

    fun createTemplate() {
        if (!currentState.canCreateTemplate) return
        updateState { copy(isLoading = true) }

        launch {
            val title = currentState.bottariTitle
            val items = currentState.currentBottariItems.map { it.name }
            createBottariTemplateUseCase(title, items)
                .onSuccess { createdTemplateId ->
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_UPLOAD,
                        mapOf(
                            "template_id" to createdTemplateId,
                            "template_title" to title,
                            "template_items" to items.toString(),
                        ),
                    )
                    emitEvent(TemplateCreateUiEvent.CreateTemplateSuccuss)
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.NotFoundException -> emitEvent(TemplateCreateUiEvent.CreateTemplateFailure.NotFoundException)
                        BottariException.InvalidException -> emitEvent(TemplateCreateUiEvent.CreateTemplateFailure.InvalidException)
                        BottariException.DuplicatedException -> emitEvent(TemplateCreateUiEvent.CreateTemplateFailure.DuplicatedException)
                        else -> emitEvent(TemplateCreateUiEvent.CreateTemplateFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(TemplateCreateUiEvent.CreateTemplateFailure.UnexpectedException) }
        }
    }

    private fun fetchBottariDetails() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariDetailsUseCase()
                .onSuccess { handleFetchBottariDetails(it) }
                .onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.NotFoundException -> emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure.NotFoundException)
                        else -> emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure.UnexpectedException) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun handleFetchBottariDetails(bottaries: List<Bottari>) {
        val myBottaries = bottaries.map { SelectableBottariUiModel.fromDomain(it) }
        val selectedBottariId = myBottaries.firstOrNull()?.id
        updateState {
            copy(
                selectedBottariId = selectedBottariId,
                bottaries = myBottaries.updateBottariSelectedState(selectedBottariId),
            )
        }
    }

    private fun List<SelectableBottariUiModel>.updateBottariSelectedState(bottariId: Long?): List<SelectableBottariUiModel> =
        this.map { if (it.id == bottariId) it.copy(isSelected = true) else it.copy(isSelected = false) }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    TemplateCreateViewModel(
                        BottariUseCaseProvider.fetchBottariDetailsUseCase,
                        BottariTemplateUseCaseProvider.createBottariTemplateUseCase,
                    )
                }
            }
    }
}
