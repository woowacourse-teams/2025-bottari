package com.bottari.presentation.view.edit.personal.main

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
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.bottariDetail.FetchBottariDetailUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.launch

class PersonalBottariEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val fetchBottariDetailUseCase: FetchBottariDetailUseCase,
    private val toggleAlarmStateUseCase: ToggleAlarmStateUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<PersonalBottariEditUiState> =
        MutableLiveData(
            PersonalBottariEditUiState(
                id = savedStateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID_MISSING),
            ),
        )
    val uiState: LiveData<PersonalBottariEditUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<PersonalBottariEditUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<PersonalBottariEditUiEvent> get() = _uiEvent

    private val ssaid: String =
        savedStateHandle[KEY_SSAID] ?: error(ERROR_SSAID_MISSING)

    init {
        fetchBottari()
    }

    private val debouncedAlarmState: ((Boolean) -> Unit) =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { isActive -> toggleAlarmState(isActive) }

    fun fetchBottari() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            fetchBottariDetailUseCase(
                _uiState.value?.id ?: error(ERROR_BOTTARI_ID_MISSING),
                ssaid,
            ).onSuccess {
                _uiState.update { PersonalBottariEditUiState.from(it.toUiModel()) }
            }.onFailure {
                _uiEvent.value = PersonalBottariEditUiEvent.FetchBottariFailure
            }

            _uiState.update { copy(isLoading = false) }
        }
    }

    fun createBottariTemplate() {
        val title = _uiState.value?.title ?: return
        val items = _uiState.value?.items?.map { it.name } ?: return

        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            createBottariTemplateUseCase(ssaid, title, items)
                .onSuccess {
                    _uiEvent.value = PersonalBottariEditUiEvent.CreateTemplateSuccess
                }.onFailure {
                    _uiEvent.value = PersonalBottariEditUiEvent.CreateTemplateFailure
                }

            _uiState.update { copy(isLoading = false) }
        }
    }

    fun updateAlarmState() {
        val isActive =
            _uiState.value
                ?.alarm
                ?.isActive
                ?.not() ?: return
        debouncedAlarmState(isActive)
    }

    private fun toggleAlarmState(isActive: Boolean) {
        val alarmId = _uiState.value?.alarm?.id ?: return

        viewModelScope.launch {
            toggleAlarmStateUseCase(ssaid, alarmId, isActive)
                .onSuccess { _uiState.update { copy(alarm = alarm?.copy(isActive = isActive)) } }
                .onFailure {
                    _uiState.update { copy(alarm = alarm?.copy(isActive = isActive.not())) }
                    _uiEvent.value = PersonalBottariEditUiEvent.ToggleAlarmStateFailure
                }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"

        private const val DEBOUNCE_DELAY = 500L

        private const val ERROR_SSAID_MISSING = "[ERROR] SSAID를 확인할 수 없습니다"
        private const val ERROR_BOTTARI_ID_MISSING = "[ERROR] 보따리 Id가 없습니다"

        fun Factory(
            ssaid: String,
            bottariId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
                    stateHandle[KEY_BOTTARI_ID] = bottariId

                    PersonalBottariEditViewModel(
                        stateHandle,
                        UseCaseProvider.fetchBottariDetailUseCase,
                        UseCaseProvider.toggleAlarmStateUseCase,
                        UseCaseProvider.createBottariTemplateUseCase,
                    )
                }
            }
    }
}
