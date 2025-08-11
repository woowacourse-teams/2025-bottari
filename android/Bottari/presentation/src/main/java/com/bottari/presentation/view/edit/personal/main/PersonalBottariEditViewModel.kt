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
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
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
            createBottariTemplateUseCase(currentState.title, items)
                .onSuccess { createdTemplateId ->
                    if (createdTemplateId == null) return@onSuccess
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_UPLOAD,
                        mapOf(
                            "template_id" to createdTemplateId,
                            "template_title" to currentState.title,
                            "template_items" to items.toString(),
                        ),
                    )
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
            toggleAlarmStateUseCase(alarmId, isActive)
                .onSuccess {
                    BottariLogger.ui(
                        if (isActive) UiEventType.ALARM_ACTIVE else UiEventType.ALARM_INACTIVE,
                        mapOf("alarm_id" to alarmId),
                    )
                    updateState { copy(alarm = alarm?.copy(isActive = isActive)) }
                }.onFailure {
                    updateState { copy(alarm = alarm?.copy(isActive = isActive.not())) }
                    emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure)
                }
        }
    }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID_MISSING = "[ERROR] 보따리 Id가 없습니다"

        private const val DEBOUNCE_DELAY = 500L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
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
