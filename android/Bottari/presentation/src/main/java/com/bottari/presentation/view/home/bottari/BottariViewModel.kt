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
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.update
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import kotlinx.coroutines.launch

class BottariViewModel(
    stateHandle: SavedStateHandle,
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
) : ViewModel() {
    private val ssaid: String = stateHandle.get<String>(KEY_SSAID)!!
    private val _uiState: MutableLiveData<BottariUiState> = MutableLiveData(BottariUiState())
    val uiState: LiveData<BottariUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<BottariUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<BottariUiEvent> get() = _uiEvent

    init {
        fetchBottaries()
    }

    fun fetchBottaries() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            fetchBottariesUseCase(ssaid)
                .onSuccess { bottaries ->
                    _uiState.update {
                        copy(
                            isLoading = false,
                            bottaries = bottaries.map { bottari -> bottari.toUiModel() },
                        )
                    }
                }.onFailure {
                    _uiEvent.value = BottariUiEvent.FetchBottariesFailure
                }
        }
    }

    fun deleteBottari(bottariId: Long) {
        viewModelScope.launch {
            deleteBottariUseCase(ssaid, bottariId)
                .onSuccess {
                    fetchBottaries()
                    _uiEvent.value = BottariUiEvent.BottariDeleteSuccess
                }.onFailure {
                    _uiEvent.value = BottariUiEvent.BottariDeleteFailure
                }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    BottariViewModel(
                        stateHandle,
                        UseCaseProvider.fetchBottariesUseCase,
                        UseCaseProvider.deleteBottariUseCase,
                    )
                }
            }
    }
}
