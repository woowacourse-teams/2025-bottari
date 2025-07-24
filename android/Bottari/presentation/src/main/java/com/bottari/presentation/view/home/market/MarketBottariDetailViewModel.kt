package com.bottari.presentation.view.home.market

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
import com.bottari.presentation.base.UiState
import com.bottari.presentation.mapper.BottariTemplateMapper.toUiModel
import com.bottari.presentation.model.BottariTemplateUiModel
import kotlinx.coroutines.launch

class MarketBottariDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val fetchBottariTemplateDetailUseCase: FetchBottariTemplateDetailUseCase,
    private val takeBottariTemplateDetailUseCase: TakeBottariTemplateDetailUseCase,
) : ViewModel() {
    private val _bottariTemplate: MutableLiveData<UiState<BottariTemplateUiModel>> =
        MutableLiveData(UiState.Loading)
    val bottariTemplate: LiveData<UiState<BottariTemplateUiModel>> get() = _bottariTemplate

    private val bottariId: Long =
        savedStateHandle.get<Long>(EXTRA_BOTTARI_ID) ?: error(ERROR_BOTTARI_ID_MISSING)

    private val ssaid: String =
        savedStateHandle.get<String>(EXTRA_SSAID) ?: error(ERROR_SSAID_MISSING)

    private val _createSuccess = MutableLiveData<UiState<Long?>>()
    val createSuccess: MutableLiveData<UiState<Long?>> = _createSuccess

    init {
        fetchBottariTemplateDetail()
    }

    fun takeBottariTemplate() {
        viewModelScope.launch {
            takeBottariTemplateDetailUseCase(ssaid, bottariId)
                .onSuccess {
                    _createSuccess.value = UiState.Success(it)
                }.onFailure { _createSuccess.value = UiState.Failure(it.message) }
        }
    }

    private fun fetchBottariTemplateDetail() {
        _bottariTemplate.value = UiState.Loading
        viewModelScope.launch {
            fetchBottariTemplateDetailUseCase(bottariId)
                .onSuccess { template ->
                    _bottariTemplate.value = UiState.Success(template.toUiModel())
                }.onFailure { error ->
                    _bottariTemplate.value = UiState.Failure(error.message)
                }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "EXTRA_SSAID"
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID_MISSING = "보따리 Id가 없습니다"
        private const val ERROR_SSAID_MISSING = "SSAID를 확인할 수 없습니다"

        fun Factory(
            ssaid: String,
            bottarID: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val savedStateHandle = this.createSavedStateHandle()
                    savedStateHandle[EXTRA_BOTTARI_ID] = bottarID
                    savedStateHandle[EXTRA_SSAID] = ssaid
                    MarketBottariDetailViewModel(
                        savedStateHandle = savedStateHandle,
                        fetchBottariTemplateDetailUseCase = UseCaseProvider.fetchBottariTemplateDetailUseCase,
                        takeBottariTemplateDetailUseCase = UseCaseProvider.takeBottariTemplateDetailUseCase,
                    )
                }
            }
    }
}
