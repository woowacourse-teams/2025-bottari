package com.bottari.presentation.view.home.bottari

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
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariUiModel
import kotlinx.coroutines.launch

class BottariViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariesUseCase: FetchBottariesUseCase,
) : ViewModel() {
    private val _bottaries: MutableLiveData<UiState<List<BottariUiModel>>> =
        MutableLiveData(UiState.Loading)
    val bottaries: LiveData<UiState<List<BottariUiModel>>> get() = _bottaries

    init {
        val ssaid = stateHandle.get<String>(EXTRA_SSAID) ?: error("SSAID를 확인할 수 없음")
        fetchBottaries(ssaid)
    }

    private fun fetchBottaries(ssaid: String) {
        viewModelScope.launch {
            fetchBottariesUseCase(ssaid)
                .onSuccess { bottaries ->
                    val bottariUiModels = bottaries.map { bottari -> bottari.toUiModel() }
                    _bottaries.value = UiState.Success(bottariUiModels)
                }
                .onFailure {
                    _bottaries.value = UiState.Failure(it.message)
                }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "SSAID"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_SSAID] = ssaid
                    BottariViewModel(stateHandle, UseCaseProvider.fetchBottariesUseCase)
                }
            }
    }

}

