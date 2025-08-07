package com.bottari.presentation.view.template.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariMapper.toMyBottariUiModel
import com.bottari.presentation.model.MyBottariUiModel
import kotlinx.coroutines.launch

class TemplateCreateViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariDetailsUseCase: FetchBottariDetailsUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : BaseViewModel<TemplateCreateUiState, TemplateCreateUiEvent>(TemplateCreateUiState()) {
    private val ssaid: String = stateHandle[KEY_SSAID]!!

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
            createBottariTemplateUseCase(ssaid, title, items)
                .onSuccess { emitEvent(TemplateCreateUiEvent.CreateTemplateSuccuss) }
                .onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(TemplateCreateUiEvent.CreateTemplateFailure)
                }
        }
    }

    private fun fetchBottariDetails() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariDetailsUseCase(ssaid)
                .onSuccess { handleFetchBottariDetails(it) }
                .onFailure { emitEvent(TemplateCreateUiEvent.FetchMyBottariesFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    private fun handleFetchBottariDetails(bottaries: List<BottariDetail>) {
        val myBottaries = bottaries.map { it.toMyBottariUiModel() }
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
        private const val KEY_SSAID = "KEY_SSAID"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    TemplateCreateViewModel(
                        stateHandle,
                        UseCaseProvider.fetchBottariDetailsUseCase,
                        UseCaseProvider.createBottariTemplateUseCase,
                    )
                }
            }
    }
}
