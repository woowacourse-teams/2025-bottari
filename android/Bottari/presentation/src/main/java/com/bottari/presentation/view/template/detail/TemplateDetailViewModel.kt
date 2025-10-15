package com.bottari.presentation.view.template.detail

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TemplateDetailViewModel @Inject constructor(
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
            val items = currentState.items.map { it.name }
            takeBottariTemplateDetailUseCase(currentState.templateId, currentState.title, items)
                .onSuccess { createdBottariId ->
                    logTemplateTaken()
                    emitEvent(
                        TemplateDetailUiEvent.TakeBottariTemplateSuccess(createdBottariId),
                    )
                }.onFailure {
                    emitEvent(TemplateDetailUiEvent.TakeBottariTemplateFailure)
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
                }.onFailure {
                    emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun logTemplateTaken() {
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
        const val KEY_TEMPLATE_ID = "KEY_TEMPLATE_ID"
        private const val ERROR_REQUIRE_TEMPLATE_ID = "[ERROR] 템플릿 ID가 존재하지 않습니다"
    }
}
