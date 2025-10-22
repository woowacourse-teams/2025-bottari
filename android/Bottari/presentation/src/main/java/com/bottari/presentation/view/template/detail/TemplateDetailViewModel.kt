package com.bottari.presentation.view.template.detail

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.usecase.bookmark.FindBookmarkUseCase
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.BottariTemplateItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TemplateDetailViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    private val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase,
    private val findBookmarkUseCase: FindBookmarkUseCase,
    private val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase,
) : FlowBaseViewModel<TemplateDetailUiState, TemplateDetailUiEvent>(
        TemplateDetailUiState(
            templateId = stateHandle[KEY_TEMPLATE_ID] ?: error(ERROR_REQUIRE_TEMPLATE_ID),
        ),
    ) {
    private val isBookmark: Boolean by lazy { stateHandle.get<Boolean>(KEY_IS_BOOKMARK) ?: false }

    init {
        if (isBookmark) fetchBookmark() else fetchBottariTemplateDetail()
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

    private fun fetchBookmark() {
        updateState { copy(isLoading = true) }

        launch {
            findBookmarkUseCase(currentState.templateId)
                .onSuccess { template ->
                    template ?: return@onSuccess

                    template.items
                        .mapIndexed { index, item ->
                            BottariTemplateItemUiModel(index.toLong(), item)
                        }.also { uiModels ->
                            updateState { copy(title = template.title, items = uiModels) }
                        }
                }.onFailure { emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
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
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
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
        private const val ERROR_REQUIRE_TEMPLATE_ID = "[ERROR] 템플릿 ID가 존재하지 않습니다"
        const val KEY_TEMPLATE_ID = "KEY_TEMPLATE_ID"
        const val KEY_IS_BOOKMARK = "KEY_IS_BOOKMARK"
    }
}
