package com.bottari.presentation.compose.home.template.my

import com.bottari.core.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.core.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTemplateViewModel @Inject constructor(
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : FlowBaseViewModel<MyTemplateUiState, MyTemplateUiEvent>(MyTemplateUiState()) {
    init {
        fetchMyTemplates()
    }

    fun fetchMyTemplates() {
        launch {
            updateState { copy(isLoading = true) }

            fetchMyBottariTemplatesUseCase()
                .onSuccess { templates ->
                    val newTemplates = templates.map(BottariTemplateUiModel::fromDomain)
                    updateState { copy(templates = newTemplates) }
                }.onFailure { exception ->
                    emitEvent(MyTemplateUiEvent.FetchBottariTemplatesFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isFetched = true) } }
    }

    fun deleteTemplate(templateId: Long) {
        if (findTemplateById(templateId) == null) return

        launch {
            updateState { copy(isLoading = true) }

            deleteMyBottariTemplateUseCase(templateId)
                .onSuccess {
                    val filtered =
                        currentState.templates.filterNot { template -> template.id == templateId }
                    updateState { copy(templates = filtered) }
                }.onFailure { exception ->
                    emitEvent(MyTemplateUiEvent.DeleteTemplateFailure)
                    BottariLogger.error(exception.message, exception)
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun findTemplateById(templateId: Long): BottariTemplateUiModel? =
        currentState.templates.find { template -> template.id == templateId }
}
