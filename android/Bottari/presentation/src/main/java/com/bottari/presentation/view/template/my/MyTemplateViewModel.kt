package com.bottari.presentation.view.template.my

import com.bottari.domain.usecase.template.DeleteMyBottariTemplateUseCase
import com.bottari.domain.usecase.template.FetchMyBottariTemplatesUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyTemplateViewModel @Inject constructor(
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
                .onSuccess { updateState { copy(bottariTemplates = it.map { BottariTemplateUiModel.fromDomain(it) }) } }
                .onFailure { emitEvent(MyTemplateUiEvent.FetchMyTemplateFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }
}
