package com.bottari.feature.personal.edit

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.bottari.core.domain.model.notification.Notification
import com.bottari.core.domain.usecase.bottari.FindBottariUseCase
import com.bottari.core.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.alarm.AlarmUiModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
@HiltViewModel(assistedFactory = PersonalBottariEditViewModel.Factory::class)
class PersonalBottariEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val findBottariUseCase: FindBottariUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : BaseViewModel<PersonalBottariEditUiState, PersonalBottariEditUiEvent>(
        PersonalBottariEditUiState(bottariId = bottariId),
    ) {
    init {
        findBottari()
    }

    private fun findBottari() {
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
            createBottariTemplateUseCase(currentState.bottariTitle, "", items, emptyList())
                .onSuccess { createdTemplateId ->
                    handleCreateTemplateSuccess(createdTemplateId)
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateSuccess)
                }.onFailure {
                    emitEvent(PersonalBottariEditUiEvent.CreateTemplateFailure)
                }

            updateState { copy(isLoading = false) }
        }
    }

    private fun handleCreateTemplateSuccess(createdTemplateId: Long) {
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

    private fun createNotification(alarm: AlarmUiModel): Notification =
        Notification(
            bottariId = currentState.bottariId,
            bottariTitle = currentState.bottariTitle,
            alarm = alarm.toDomain(),
        )

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): PersonalBottariEditViewModel
    }
}
