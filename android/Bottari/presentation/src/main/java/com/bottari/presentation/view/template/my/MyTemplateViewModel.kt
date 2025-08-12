package com.bottari.presentation.view.template.my

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel

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
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_DELETE,
                        mapOf(
                            "template_id" to bottariTemplateId,
                            "template_title" to template?.title.orEmpty(),
                            "template_items" to template?.items.toString(),
                        ),
                    )
                    updateState { copy(bottariTemplates = bottariTemplates.filterNot { it.id == bottariTemplateId }) }
                    emitEvent(MyTemplateUiEvent.DeleteMyTemplateSuccess)
                }.onFailure {
                    emitEvent(MyTemplateUiEvent.DeleteMyTemplateFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchMyBottariTemplates() {
        updateState { copy(isLoading = true) }

        launch {
            fetchMyBottariTemplatesUseCase()
                .onSuccess { updateState { copy(bottariTemplates = it.map { it.toUiModel() }) } }
                .onFailure { emitEvent(MyTemplateUiEvent.FetchMyTemplateFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MyTemplateViewModel(
                        UseCaseProvider.fetchMyBottariTemplatesUseCase,
                        UseCaseProvider.deleteMyBottariTemplateUseCase,
                    )
                }
            }
    }
}
