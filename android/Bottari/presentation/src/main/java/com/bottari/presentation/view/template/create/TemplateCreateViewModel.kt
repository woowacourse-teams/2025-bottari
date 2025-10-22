package com.bottari.presentation.view.template.create

import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.bottari.personal.PersonalBottari
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.template.SelectableBottariUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TemplateCreateViewModel @Inject constructor(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
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
            createBottariTemplateUseCase(title, "", items, emptyList())
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
        fetchBottariesUseCase()
            .onEach(::handleFetchBottaries)
            .catch {
                emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun handleFetchBottaries(bottaries: List<PersonalBottari>) {
        val myBottaries =
            bottaries.filterNot { bottari -> bottari.items.isEmpty() }.map(
                SelectableBottariUiModel::fromDomain,
            )
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
}
