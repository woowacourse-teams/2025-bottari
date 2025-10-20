package com.bottari.presentation.view.edit.personal.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.usecase.alarm.UpdateAlarmActivateUseCase
import com.bottari.domain.usecase.bottari.FindBottariUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.util.debounce
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PersonalBottariEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val findBottariUseCase: FindBottariUseCase,
    private val updateAlarmActivateUseCase: UpdateAlarmActivateUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
    private val alarmScheduler: AlarmScheduler,
) : FlowBaseViewModel<PersonalBottariEditUiState, PersonalBottariEditUiEvent>(
        PersonalBottariEditUiState(
            bottariId = savedStateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID),
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

    fun changeBottariRenameDialogState(shouldShow: Boolean) {
        updateState { copy(showBottariRenameDialog = shouldShow) }
    }

    private fun toggleAlarmState(newActiveState: Boolean) {
        val alarm = currentState.alarm ?: return
        if (alarm.isActive == newActiveState) return

        launch {
            updateAlarmActivateUseCase(
                currentState.bottariId,
                newActiveState,
            ).onSuccess {
                val newAlarm = alarm.copy(isActive = newActiveState)
                handleAlarm(newAlarm)
                updateState {
                    copy(
                        isAlarmActive = newActiveState,
                        alarm = newAlarm,
                    )
                }
                BottariLogger.ui(
                    if (newActiveState) UiEventType.ALARM_ACTIVE else UiEventType.ALARM_INACTIVE,
                    mapOf("alarm_id" to alarm.id!!),
                )
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

    private fun handleAlarm(alarm: AlarmUiModel) {
        val notification = createNotification(alarm)
        if (alarm.isActive) {
            alarmScheduler.scheduleAlarm(notification)
            return
        }
        alarmScheduler.cancelAlarm(notification)
    }

    private fun createNotification(alarm: AlarmUiModel): Notification =
        Notification(
            bottariId = currentState.bottariId,
            bottariTitle = currentState.bottariTitle,
            alarm = alarm.toDomain(),
        )

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 없습니다"
        private const val DEBOUNCE_DELAY = 700L
    }
}
