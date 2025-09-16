package com.bottari.presentation.view.template.create

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.MyBottariUiModel

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
                    if (createdTemplateId == null) return@onSuccess
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_UPLOAD,
                        mapOf(
                            "template_id" to createdTemplateId,
                            "template_title" to title,
                            "template_items" to items.toString(),
                        ),
                    )
                    emitEvent(TemplateCreateUiEvent.CreateTemplateSuccuss)
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(TemplateCreateUiEvent.CreateTemplateFailure)
                }
        }
    }

    private fun fetchBottariDetails() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariDetailsUseCase()
                .onSuccess { handleFetchBottariDetails(it) }
                .onFailure { emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun handleFetchBottariDetails(bottaries: List<Bottari>) {
        val myBottaries = bottaries.map { MyBottariUiModel.fromDomain(it) }
        val selectedBottariId = myBottaries.firstOrNull()?.id
        updateState {
            copy(
                selectedBottariId = selectedBottariId,
                bottaries = myBottaries.updateBottariSelectedState(selectedBottariId),
            )
        }
    }

    private fun List<MyBottariUiModel>.updateBottariSelectedState(bottariId: Long?): List<MyBottariUiModel> =
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
