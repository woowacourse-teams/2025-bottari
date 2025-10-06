package com.bottari.presentation.view.edit.personal.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.AlarmUseCaseProvider
import com.bottari.di.usecase.BottariTemplateUseCaseProvider
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.domain.usecase.alarm.UpdateAlarmActivateUseCase
import com.bottari.domain.usecase.bottari.FindBottariUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.NotificationUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PersonalBottariEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val findBottariUseCase: FindBottariUseCase,
    private val updateAlarmActivateUseCase: UpdateAlarmActivateUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : FlowBaseViewModel<PersonalBottariEditUiState, PersonalBottariEditUiEvent>(
        PersonalBottariEditUiState(
            bottariId = savedStateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID_MISSING),
        ),
    ) {
    private val debouncedAlarmState: ((Boolean) -> Unit) =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { isActive -> toggleAlarmState(isActive) }

    init {
        findBottari()
    }

    fun findBottari() {
        updateState { copy(isLoading = true) }
        findBottariUseCase(currentState.bottariId)
            .onEach { bottari ->
                if (bottari == null) {
                    emitEvent(PersonalBottariEditUiEvent.FindBottariFailure)
                    return@onEach
                }
                updateState { PersonalBottariEditUiState.from(bottari) }
            }.catch {
                emitEvent(PersonalBottariEditUiEvent.FindBottariFailure)
                updateState { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    fun createBottariTemplate() {
        if (currentState.bottariTitle.isBlank()) return
        updateState { copy(isLoading = true) }

        val items = currentState.items.map { it.name }
        launch {
            createBottariTemplateUseCase(currentState.bottariTitle, items)
                .onSuccess { createdTemplateId ->
                    handleCreateTemplateSuccess(createdTemplateId)
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateSuccess)
                }.onFailure {
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    fun updateAlarmState() {
        val isActive = currentState.isAlarmActive.not()
        updateState { copy(isAlarmActive = isActive) }
        debouncedAlarmState(isActive)
    }

    private fun toggleAlarmState(newActiveState: Boolean) {
        val alarm = currentState.alarm ?: return
        if (alarm.isActive == newActiveState) return

        launch {
            updateAlarmActivateUseCase(
                currentState.bottariId,
                newActiveState,
            ).onSuccess {
                handleAlarmStateChanged(newActiveState, alarm)
            }.onFailure {
                emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure)
            }
        }
    }

    private fun handleCreateTemplateSuccess(createdTemplateId: Long?) {
        if (createdTemplateId == null) return

        val itemNames = currentState.items.map { it.name }
        BottariLogger.ui(
            UiEventType.TEMPLATE_UPLOAD,
            mapOf(
                "template_id" to createdTemplateId,
                "template_title" to currentState.bottariTitle,
                "template_items" to itemNames.toString(),
            ),
        )
    }

    private fun handleAlarmStateChanged(
        newActiveState: Boolean,
        alarm: AlarmUiModel,
    ) {
        BottariLogger.ui(
            if (newActiveState) UiEventType.ALARM_ACTIVE else UiEventType.ALARM_INACTIVE,
            mapOf("alarm_id" to alarm.id!!),
        )
        scheduleAlarm(newActiveState, alarm)
        updateState {
            copy(
                isAlarmActive = newActiveState,
                alarm = alarm.copy(isActive = newActiveState),
            )
        }
    }

    private fun scheduleAlarm(
        isActive: Boolean,
        alarm: AlarmUiModel,
    ) {
        val notification = createNotification(alarm)
        if (isActive) {
            AlarmScheduler.scheduleAlarm(notification = notification.toDomain())
            return
        }
        AlarmScheduler.cancelAlarm(notification = notification.toDomain())
    }

    private fun createNotification(alarm: AlarmUiModel): NotificationUiModel =
        NotificationUiModel(
            bottariId = currentState.bottariId,
            bottariTitle = currentState.bottariTitle,
            alarm = alarm,
        )

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID_MISSING = "[ERROR] 보따리 Id가 없습니다"

        private const val DEBOUNCE_DELAY = 700L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId

                    PersonalBottariEditViewModel(
                        stateHandle,
                        BottariUseCaseProvider.findBottariUseCase,
                        AlarmUseCaseProvider.updateAlarmActivateUseCase,
                        BottariTemplateUseCaseProvider.createBottariTemplateUseCase,
                    )
                }
            }
    }
}
