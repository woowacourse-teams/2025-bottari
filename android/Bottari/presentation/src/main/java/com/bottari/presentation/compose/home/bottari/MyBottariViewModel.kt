package com.bottari.presentation.compose.home.bottari

import androidx.lifecycle.viewModelScope
import com.bottari.domain.model.notification.Notification
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.domain.usecase.team.ExitTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.MyBottariUiModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel
import com.bottari.presentation.util.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MyBottariViewModel @Inject constructor(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase,
    private val createBottariUseCase: CreateBottariUseCase,
    private val createTeamBottariUseCase: CreateTeamBottariUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
    private val deleteTeamBottariUseCase: ExitTeamBottariUseCase,
    private val joinTeamBottariUseCase: JoinTeamBottariUseCase,
    private val alarmScheduler: AlarmScheduler,
) : FlowBaseViewModel<MyBottariUiState, MyBottariUiEvent>(MyBottariUiState()) {
    init {
        fetchPersonalBottaries()
    }

    fun fetchTeamBottaries() {
        updateState { copy(isLoading = true) }

        launch {
            fetchTeamBottariesUseCase()
                .onSuccess { bottaries ->
                    updateState { copy(teamBottaries = bottaries.map(TeamBottariUiModel::fromDomain)) }
                }.onFailure { emitEvent(MyBottariUiEvent.FetchBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false, isTeamFetched = true) } }
    }

    fun deletePersonalBottari(bottariId: Long) {
        val bottari =
            currentState.allBottaries.find { bottari -> bottari.id == bottariId } ?: return
        updateState { copy(isLoading = true) }

        launch {
            deleteBottariUseCase(bottariId)
                .onSuccess {
                    cancelAlarm(bottari)
                    emitEvent(MyBottariUiEvent.DeletePersonalBottariSuccess)
                }.onFailure { emitEvent(MyBottariUiEvent.DeletePersonalBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    fun deleteTeamBottari(bottariId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariUseCase(bottariId)
                .onSuccess {
                    fetchTeamBottaries()
                    emitEvent(MyBottariUiEvent.ExitTeamBottariSuccess)
                }.onFailure { emitEvent(MyBottariUiEvent.ExitTeamBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    fun onClickDialog(text: String) {
        uiState.value.showDialogType?.let { type ->
            when (type) {
                MyBottariDialogType.CODE -> inputTeamBottariCode(text)
                MyBottariDialogType.PERSONAL -> createPersonalBottari(text)
                MyBottariDialogType.TEAM -> createTeamBottari(text)
            }
        }
    }

    fun openDialog(type: MyBottariDialogType) {
        updateState { copy(showDialogType = type) }
    }

    fun closeDialog() {
        updateState { copy(showDialogType = null) }
    }

    private fun inputTeamBottariCode(code: String) {
        updateState { copy(isLoading = true) }

        launch {
            closeDialog()
            joinTeamBottariUseCase(code)
                .onSuccess { fetchTeamBottaries() }
                .onFailure { emitEvent(MyBottariUiEvent.JoinTeamBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun createPersonalBottari(title: String) {
        updateState { copy(isLoading = true) }

        launch {
            closeDialog()
            createBottariUseCase(title)
                .onSuccess { bottariId ->
                    emitEvent(MyBottariUiEvent.CreatePersonalBottariSuccess(bottariId))
                }.onFailure { emitEvent(MyBottariUiEvent.CreateBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun createTeamBottari(title: String) {
        updateState { copy(isLoading = true) }

        launch {
            closeDialog()
            createTeamBottariUseCase(title)
                .onSuccess { bottariId ->
                    bottariId?.let { id ->
                        emitEvent(MyBottariUiEvent.CreateTeamBottariSuccess(id))
                        fetchTeamBottaries()
                    }
                }.onFailure { emitEvent(MyBottariUiEvent.CreateBottariFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun cancelAlarm(bottari: MyBottariUiModel) =
        bottari.alarm?.let { alarm ->
            alarmScheduler.cancelAlarm(
                notification =
                    Notification(
                        bottariId = bottari.id,
                        bottariTitle = bottari.title,
                        alarm = alarm.toDomain(),
                    ),
            )
        }

    private fun fetchPersonalBottaries() =
        fetchBottariesUseCase()
            .catch { emitEvent(MyBottariUiEvent.FetchBottariFailure) }
            .onEach { bottaries ->
                updateState {
                    copy(
                        personalBottaries = bottaries.map(BottariUiModel::fromPersonalBottari),
                        isPersonalFetched = true,
                    )
                }
            }.launchIn(viewModelScope)
}
