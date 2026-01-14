package com.bottari.feature.mybottari

import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import com.bottari.core.domain.network.NetworkManager
import com.bottari.core.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.core.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.core.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.core.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.core.domain.usecase.team.ExitTeamBottariUseCase
import com.bottari.core.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.core.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.core.ui.base.NetworkBaseViewModel
import com.bottari.feature.mybottari.component.MyBottariDialogType
import com.bottari.feature.mybottari.model.BottariUiModel
import com.bottari.feature.mybottari.model.MyBottariUiModel
import com.bottari.feature.mybottari.model.TeamBottariUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@Stable
@HiltViewModel
class MyBottariViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase,
    private val createBottariUseCase: CreateBottariUseCase,
    private val createTeamBottariUseCase: CreateTeamBottariUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
    private val deleteTeamBottariUseCase: ExitTeamBottariUseCase,
    private val joinTeamBottariUseCase: JoinTeamBottariUseCase,
//    private val alarmScheduler: AlarmScheduler,
) : NetworkBaseViewModel<MyBottariUiState, MyBottariUiEvent>(
        initialState = MyBottariUiState(),
        networkManager = networkManager,
    ) {
    init {
        fetchPersonalBottaries()
    }

    fun fetchTeamBottaries() {
        if (isConnected.value.not()) {
            updateState { copy(isTeamFetched = true) }
            return
        }

        launch {
            updateState { copy(isLoading = true) }
            fetchTeamBottariesUseCase()
                .onSuccess { bottaries ->
                    updateState { copy(teamBottaries = bottaries.map(TeamBottariUiModel::fromDomain)) }
                }.onFailure {
                    emitEvent(MyBottariUiEvent.FetchBottariFailure)
                }
        }.invokeOnCompletion {
            updateState {
                MyBottariUiState(
                    isLoading = false,
                    isTeamFetched = true,
                )
            }
        }
    }

    fun deletePersonalBottari(bottariId: Long) {
        val bottari =
            currentState.allBottaries.find { bottari -> bottari.id == bottariId } ?: return

        launch {
            deleteBottariUseCase(bottariId)
                .onSuccess {
                    cancelAlarm(bottari)
                    emitEvent(MyBottariUiEvent.DeletePersonalBottariSuccess)
                }.onFailure { emitEvent(MyBottariUiEvent.DeletePersonalBottariFailure) }
        }
    }

    fun deleteTeamBottari(bottariId: Long) {
        launch {
            deleteTeamBottariUseCase(bottariId)
                .onSuccess {
                    fetchTeamBottaries()
                    emitEvent(MyBottariUiEvent.ExitTeamBottariSuccess)
                }.onFailure { emitEvent(MyBottariUiEvent.ExitTeamBottariFailure) }
        }
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
        launch {
            closeDialog()
            joinTeamBottariUseCase(code)
                .onSuccess { fetchTeamBottaries() }
                .onFailure { emitEvent(MyBottariUiEvent.JoinTeamBottariFailure) }
        }
    }

    private fun createPersonalBottari(title: String) {
        launch {
            closeDialog()
            createBottariUseCase(title)
                .onSuccess { bottariId ->
                    emitEvent(MyBottariUiEvent.CreatePersonalBottariSuccess(bottariId))
                }.onFailure { emitEvent(MyBottariUiEvent.CreateBottariFailure) }
        }
    }

    private fun createTeamBottari(title: String) {
        launch {
            closeDialog()
            createTeamBottariUseCase(title)
                .onSuccess { bottariId ->
                    bottariId?.let { id ->
                        emitEvent(MyBottariUiEvent.CreateTeamBottariSuccess(id))
                        fetchTeamBottaries()
                    }
                }.onFailure { emitEvent(MyBottariUiEvent.CreateBottariFailure) }
        }
    }

    private fun cancelAlarm(bottari: MyBottariUiModel) =
        bottari.alarm?.let { alarm ->
//            alarmScheduler.cancelAlarm(
//                notification =
//                    Notification(
//                        bottariId = bottari.id,
//                        bottariTitle = bottari.title,
//                        alarm = alarm.toDomain(),
//                    ),
//            )
        }

    private fun fetchPersonalBottaries() =
        fetchBottariesUseCase()
            .catch { emitEvent(MyBottariUiEvent.FetchBottariFailure) }
            .onEach { bottaries ->
                updateState {
                    MyBottariUiState(
                        personalBottaries = bottaries.map(BottariUiModel::fromPersonalBottari),
                        isPersonalFetched = true,
                    )
                }
            }.launchIn(viewModelScope)
}
