package com.bottari.presentation.view.edit.personal.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.bottariDetail.FetchBottariDetailUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.launch

class PersonalBottariEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val fetchBottariDetailUseCase: FetchBottariDetailUseCase,
    private val toggleAlarmStateUseCase: ToggleAlarmStateUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : BaseViewModel<PersonalBottariEditUiState, PersonalBottariEditUiEvent>(
        PersonalBottariEditUiState(
            id = savedStateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID_MISSING),
        ),
    ) {
    private val ssaid: String = savedStateHandle[KEY_SSAID] ?: error(ERROR_SSAID_MISSING)

    init {
        fetchBottari()
    }

    private val debouncedAlarmState: ((Boolean) -> Unit) =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { isActive -> toggleAlarmState(isActive) }

    fun fetchBottari() {
        updateState { copy(isLoading = true) }

        launch {
            fetchBottariDetailUseCase(
                currentState.id,
                ssaid,
            ).onSuccess {
                updateState { PersonalBottariEditUiState.from(it.toUiModel()) }
            }.onFailure {
                emitEvent(PersonalBottariEditUiEvent.FetchBottariFailure)
            }

            updateState { copy(isLoading = false) }
        }
    }

    fun createBottariTemplate() {
        if (currentState.title.isBlank()) return
        updateState { copy(isLoading = true) }

        val items = currentState.items.map { it.name }
        launch {
            createBottariTemplateUseCase(ssaid, currentState.title, items)
                .onSuccess {
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateSuccess)
                }.onFailure {
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun updateAlarmState() {
        val isActive = currentState.isAlarmActive.not()
        debouncedAlarmState(isActive)
    }

    private fun toggleAlarmState(isActive: Boolean) {
        val alarmId = currentState.alarm?.id ?: return

        launch {
            toggleAlarmStateUseCase(ssaid, alarmId, isActive)
                .onSuccess { updateState { copy(alarm = alarm?.copy(isActive = isActive)) } }
                .onFailure {
                    updateState { copy(alarm = alarm?.copy(isActive = isActive.not())) }
                    emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure)
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
