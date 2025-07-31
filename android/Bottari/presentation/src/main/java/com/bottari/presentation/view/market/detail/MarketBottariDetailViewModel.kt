package com.bottari.presentation.view.market.detail

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
import com.bottari.domain.usecase.template.FetchBottariTemplateDetailUseCase
import com.bottari.domain.usecase.template.TakeBottariTemplateDetailUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.requireValue
import com.bottari.presentation.extension.update
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import kotlinx.coroutines.launch

class MarketBottariDetailViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase,
    private val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<MarketBottariDetailUiState> =
        MutableLiveData(
            MarketBottariDetailUiState(
                templateId = stateHandle[KEY_TEMPLATE_ID] ?: error(ERROR_REQUIRE_TEMPLATE_ID),
            ),
        )
    val uiState: LiveData<MarketBottariDetailUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<MarketBottariDetailUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<MarketBottariDetailUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    init {
        fetchBottariTemplateDetail()
    }

    fun takeBottariTemplate() {
        viewModelScope.launch {
            takeBottariTemplateDetailUseCase(ssaid, _uiState.requireValue().templateId)
                .onSuccess { bottariId ->
                    _uiEvent.value =
                        MarketBottariDetailUiEvent.TakeBottariTemplateSuccess(bottariId)
                }.onFailure {
                    _uiEvent.value = MarketBottariDetailUiEvent.TakeBottariTemplateFailure
                }
        }
    }

    private fun fetchBottariTemplateDetail() {
        _uiState.update { copy(isLoading = true) }
        viewModelScope.launch {
            fetchBottariTemplateDetailUseCase(_uiState.requireValue().templateId)
                .onSuccess { template ->
                    val itemUiModels = template.items.map { it.toUiModel() }
                    _uiState.update { copy(isLoading = false, items = itemUiModels) }
                }.onFailure {
                    _uiState.update { copy(isLoading = false) }
                    _uiEvent.value = MarketBottariDetailUiEvent.FetchBottariDetailFailure
                }
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
                    MarketBottariDetailViewModel(
                        stateHandle = savedStateHandle,
                        fetchBottariTemplateDetailUseCase = UseCaseProvider.fetchBottariTemplateDetailUseCase,
                        takeBottariTemplateDetailUseCase = UseCaseProvider.takeBottariTemplateDetailUseCase,
                    )
                }
            }
    }
}
