package com.bottari.presentation.view.template.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.usecase.bottari.FetchBottariDetailsUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.BottariMapper.toMyBottariUiModel
import com.bottari.presentation.model.MyBottariUiModel
import kotlinx.coroutines.launch

class TemplateCreateViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariDetailsUseCase: FetchBottariDetailsUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<TemplateCreateUiState> =
        MutableLiveData(
            TemplateCreateUiState(),
        )
    val uiState: LiveData<TemplateCreateUiState> = _uiState

    private val _uiEvent: SingleLiveEvent<TemplateCreateUiEvent> = SingleLiveEvent()
    val uiEvent: SingleLiveEvent<TemplateCreateUiEvent> = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID]!!

    init {
        fetchBottariDetails()
    }

    fun changeBottari(bottariId: Long) {
        _uiState.update {
            copy(
                selectedBottariId = bottariId,
                bottaries = bottaries.updateBottariSelectedState(bottariId),
            )
        }
    }

    fun createTemplate() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            val title = _uiState.value!!.bottariTitle
            val items = _uiState.value!!.selectedBottari.map { it.name }
            createBottariTemplateUseCase(ssaid, title, items)
                .onSuccess { _uiEvent.value = TemplateCreateUiEvent.CreateTemplateSuccuss }
                .onFailure {
                    _uiState.update { copy(isLoading = false) }
                    _uiEvent.value = TemplateCreateUiEvent.CreateTemplateFailure
                }
        }
    }

    private fun fetchBottariDetails() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            fetchBottariDetailsUseCase(ssaid)
                .onSuccess { handleFetchBottariDetails(it) }
                .onFailure { _uiEvent.value = TemplateCreateUiEvent.FetchMyBottariesFailure }

            _uiState.update { copy(isLoading = false) }
        }
    }

    private fun handleFetchBottariDetails(bottaries: List<BottariDetail>) {
        val myBottaries = bottaries.map { it.toMyBottariUiModel() }
        val selectedBottariId = myBottaries.firstOrNull()?.id
        _uiState.update {
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
