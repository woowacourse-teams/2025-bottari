package com.bottari.presentation.view.template.my

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
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
    stateHandle: SavedStateHandle,
    private val fetchMyBottariTemplatesUseCase: FetchMyBottariTemplatesUseCase,
    private val deleteMyBottariTemplateUseCase: DeleteMyBottariTemplateUseCase,
) : BaseViewModel<MyTemplateUiState, MyTemplateUiEvent>(MyTemplateUiState()) {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_SSAID_MISSING)

    init {
        fetchMyBottariTemplates()
    }

    fun deleteBottariTemplate(bottariTemplateId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteMyBottariTemplateUseCase(ssaid, bottariTemplateId)
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
            fetchMyBottariTemplatesUseCase(ssaid)
                .onSuccess { updateState { copy(bottariTemplates = it.map { it.toUiModel() }) } }
                .onFailure { emitEvent(MyTemplateUiEvent.FetchMyTemplateFailure) }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val ERROR_SSAID_MISSING = "[ERROR] SSAID를 확인할 수 없습니다"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    MyTemplateViewModel(
                        stateHandle,
                        UseCaseProvider.fetchMyBottariTemplatesUseCase,
                        UseCaseProvider.deleteMyBottariTemplateUseCase,
                    )
                }
            }
    }
}
