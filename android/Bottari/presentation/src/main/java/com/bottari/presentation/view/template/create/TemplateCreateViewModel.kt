package com.bottari.presentation.view.template.create

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.SelectableBottariUiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class TemplateCreateViewModel(
    private val fetchBottariDetailsUseCase: FetchBottariDetailsUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : FlowBaseViewModel<TemplateCreateUiState, TemplateCreateUiEvent>(TemplateCreateUiState()) {
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
        fetchBottariDetailsUseCase()
            .onEach(::handleFetchBottariDetails)
            .catch {
                emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun handleFetchBottariDetails(bottaries: List<PersonalBottari>) {
        val myBottaries = bottaries.map { SelectableBottariUiModel.fromDomain(it) }
        val selectedBottariId = myBottaries.firstOrNull()?.id
        updateState {
            copy(
                isLoading = false,
                selectedBottariId = selectedBottariId,
                bottaries = myBottaries.updateBottariSelectedState(selectedBottariId),
            )
        }
    }

    private fun List<SelectableBottariUiModel>.updateBottariSelectedState(bottariId: Long?): List<SelectableBottariUiModel> =
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
