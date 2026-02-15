package com.bottari.feature.template.detail

import androidx.compose.runtime.Stable
import com.bottari.core.domain.usecase.bookmark.FindBookmarkUseCase
import com.bottari.core.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.core.domain.usecase.template.TakeBottariTemplateDetailUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.template.BottariTemplateItemUiModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@Stable
@HiltViewModel(assistedFactory = TemplateDetailViewModel.Factory::class)
class TemplateDetailViewModel @AssistedInject constructor(
    @Assisted private val templateId: Long,
    @Assisted private val isBookmark: Boolean,
    private val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase,
    private val findBookmarkUseCase: FindBookmarkUseCase,
    private val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase,
) : BaseViewModel<TemplateDetailUiState, TemplateDetailUiEvent>(TemplateDetailUiState()) {
    init {
        if (isBookmark) fetchBookmark() else fetchBottariTemplateDetail()
    }

    fun takeBottariTemplate() {
        updateState { copy(isLoading = true) }

        launch {
            val items = currentState.items.map { it.name }
            takeBottariTemplateDetailUseCase(templateId, currentState.title, items)
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
            findBookmarkUseCase(templateId)
                .onSuccess { template ->
                    if (template == null) {
                        emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure)
                        return@onSuccess
                    }

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
            fetchBottariTemplateDetailUseCase(templateId)
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
                "template_id" to templateId,
                "template_title" to currentState.title,
                "template_items" to currentState.items.toString(),
            ),
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(
            templateId: Long,
            isBookmark: Boolean,
        ): TemplateDetailViewModel
    }
}
