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
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.bottariDetail.FetchBottariDetailUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.model.alarm.AlarmUiModel
import com.bottari.presentation.model.alarm.NotificationUiModel
import com.bottari.presentation.model.bottari.personal.BottariDetailUiModel
import com.bottari.presentation.util.AlarmScheduler
import com.bottari.presentation.util.debounce

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
                updateState { PersonalBottariEditUiState.from(BottariDetailUiModel.fromDomain(it)) }
            }.onApiException { bottariException ->
                when (bottariException) {
                    BottariException.PermissionException -> emitEvent(PersonalBottariEditUiEvent.FetchBottariFailure.PermissionException)
                    BottariException.NotFoundException -> emitEvent(PersonalBottariEditUiEvent.FetchBottariFailure.NotFoundException)
                    else -> emitEvent(PersonalBottariEditUiEvent.FetchBottariFailure.UnexpectedException)
                }
            }.onApiError { emitEvent(PersonalBottariEditUiEvent.FetchBottariFailure.UnexpectedException) }

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
                    BottariLogger.ui(
                        UiEventType.TEMPLATE_UPLOAD,
                        mapOf(
                            "template_id" to createdTemplateId,
                            "template_title" to currentState.title,
                            "template_items" to items.toString(),
                        ),
                    )
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateSuccess)
                }.onApiException { bottariException ->
                    when (bottariException) {
                        BottariException.InvalidException -> emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure.InvalidException)
                        BottariException.NotFoundException -> emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure.NotFoundException)
                        else -> emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure.UnexpectedException) }

            updateState { copy(isLoading = false) }
        }
    }

    fun updateAlarmState() {
        val isActive = currentState.isAlarmActive.not()
        debouncedAlarmState(isActive)
    }

    private fun toggleAlarmState(isActive: Boolean) {
        val alarm = currentState.alarm ?: return

        launch {
            toggleAlarmStateUseCase(
                currentState.id,
                currentState.title,
                alarm.toDomain(),
                isActive,
            ).onSuccess {
                BottariLogger.ui(
                    if (isActive) UiEventType.ALARM_ACTIVE else UiEventType.ALARM_INACTIVE,
                    mapOf("alarm_id" to alarm.id!!),
                )
                scheduleAlarm(isActive, alarm)
                updateState { copy(alarm = alarm.copy(isActive = isActive)) }
            }.onApiException { bottariException ->
                when (bottariException) {
                    BottariException.NotFoundException ->
                        emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure.NotFoundException)

                    BottariException.DuplicatedException ->
                        emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure.DuplicatedException)

                    else -> emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure.UnexpectedException)
                }
            }.onApiError { emitEvent(PersonalBottariEditUiEvent.ToggleAlarmStateFailure.UnexpectedException) }
        }
    }

    private fun scheduleAlarm(
        isActive: Boolean,
        alarm: AlarmUiModel,
    ) {
        val notification = createNotification(alarm)
        if (isActive) {
            AlarmScheduler.scheduleAlarm(notification = notification)
            return
        }
        AlarmScheduler.cancelAlarm(notification = notification)
    }

    private fun createNotification(alarm: AlarmUiModel): NotificationUiModel =
        NotificationUiModel(
            bottariId = currentState.id,
            bottariTitle = currentState.title,
            alarm = alarm,
        )

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
                        BottariUseCaseProvider.fetchBottariDetailUseCase,
                        AlarmUseCaseProvider.toggleAlarmStateUseCase,
                        BottariTemplateUseCaseProvider.createBottariTemplateUseCase,
                    )
                }
            }
    }
}
