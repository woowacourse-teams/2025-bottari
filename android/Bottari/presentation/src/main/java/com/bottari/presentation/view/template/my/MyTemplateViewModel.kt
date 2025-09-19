package com.bottari.presentation.view.template.my

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateUiModel

class MyTemplateViewModel(
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : BaseViewModel<MyTemplateUiState, MyTemplateUiEvent>(MyTemplateUiState()) {
    init {
        fetchMyBottariTemplates()
    }

    fun deleteBottariTemplate(bottariTemplateId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteMyBottariTemplateUseCase(bottariTemplateId)
                .onSuccess {
                    val template = currentState.bottariTemplates.find { it.id == bottariTemplateId }
                    logDeleteMyTemplate(bottariTemplateId, template)
                    updateState { copy(bottariTemplates = bottariTemplates.filterNot { it.id == bottariTemplateId }) }
                    emitEvent(MyTemplateUiEvent.DeleteMyTemplateSuccess)
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.PermissionException -> emitEvent(MyTemplateUiEvent.DeleteMyTemplateFailure.PermissionException)
                        is BottariException.NotFoundException -> emitEvent(MyTemplateUiEvent.DeleteMyTemplateFailure.NotFoundException)
                        else -> emitEvent(MyTemplateUiEvent.DeleteMyTemplateFailure.UnexpectedException)
                    }
                }.onApiError {
                    emitEvent(MyTemplateUiEvent.DeleteMyTemplateFailure.UnexpectedException)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchMyBottariTemplates() {
        updateState { copy(isLoading = true) }

        launch {
            fetchMyBottariTemplatesUseCase()
                .onSuccess { bottariTemplates ->
                    val newBottariTemplates =
                        bottariTemplates.map { BottariTemplateUiModel.fromDomain(it) }
                    updateState { copy(bottariTemplates = newBottariTemplates) }
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.NotFoundException -> emitEvent(MyTemplateUiEvent.FetchMyTemplateFailure.NotFoundException)
                        else -> emitEvent(MyTemplateUiEvent.FetchMyTemplateFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(MyTemplateUiEvent.FetchMyTemplateFailure.UnexpectedException) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    private fun logDeleteMyTemplate(
        templateId: Long,
        template: BottariTemplateUiModel?,
    ) {
        BottariLogger.ui(
            UiEventType.TEMPLATE_DELETE,
            mapOf(
                "template_id" to templateId,
                "template_title" to template?.title.orEmpty(),
                "template_items" to template?.items.toString(),
            ),
        )
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MyTemplateViewModel(
                        BottariTemplateUseCaseProvider.fetchMyBottariTemplatesUseCase,
                        BottariTemplateUseCaseProvider.deleteMyBottariTemplateUseCase,
                    )
                }
            }
    }
}
