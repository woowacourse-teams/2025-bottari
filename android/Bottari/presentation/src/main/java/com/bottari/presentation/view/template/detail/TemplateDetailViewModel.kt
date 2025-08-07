package com.bottari.presentation.view.template.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel

class TemplateDetailViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase,
    private val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase,
) : BaseViewModel<TemplateDetailUiState, TemplateDetailUiEvent>(
        TemplateDetailUiState(
            templateId = stateHandle[KEY_TEMPLATE_ID] ?: error(ERROR_REQUIRE_TEMPLATE_ID),
        ),
    ) {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    init {
        fetchBottariTemplateDetail()
    }

    fun takeBottariTemplate() {
        updateState { copy(isLoading = true) }

        launch {
            takeBottariTemplateDetailUseCase(ssaid, currentState.templateId)
                .onSuccess { createdBottariId ->
                    if (createdBottariId == null) return@onSuccess
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_TAKE,
                        mapOf(
                            "template_id" to currentState.templateId,
                            "template_title" to currentState.title,
                            "template_items" to currentState.items.toString(),
                        ),
                    )
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
                    val itemUiModels = template.items.map { it.toUiModel() }
                    updateState { copy(title = template.title, items = itemUiModels) }
                }.onFailure {
                    emitEvent(TemplateDetailUiEvent.FetchBottariDetailFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_TEMPLATE_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_TEMPLATE_ID = "[ERROR] 템플릿 ID가 존재하지 않습니다."
        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID가 존재하지 않습니다."

        fun Factory(
            ssaid: String,
            templateId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val savedStateHandle = this.createSavedStateHandle()
                    savedStateHandle[KEY_TEMPLATE_ID] = templateId
                    savedStateHandle[KEY_SSAID] = ssaid
                    TemplateDetailViewModel(
                        stateHandle = savedStateHandle,
                        fetchBottariTemplateDetailUseCase = UseCaseProvider.fetchBottariTemplateDetailUseCase,
                        takeBottariTemplateDetailUseCase = UseCaseProvider.takeBottariTemplateDetailUseCase,
                    )
                }
            }
    }
}
