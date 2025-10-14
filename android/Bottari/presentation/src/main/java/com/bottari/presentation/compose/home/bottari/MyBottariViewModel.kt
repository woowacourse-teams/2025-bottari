package com.bottari.presentation.compose.home.bottari

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.di.usecase.TeamBottariUseCaseProvider
import com.bottari.di.usecase.TeamMemberUseCaseProvider
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
import com.bottari.presentation.util.AlarmScheduler.cancelAlarm
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MyBottariViewModel(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase,
    private val createBottariUseCase: CreateBottariUseCase,
    private val createTeamBottariUseCase: CreateTeamBottariUseCase,
    private val deleteBottariUseCase: DeleteBottariUseCase,
    private val deleteTeamBottariUseCase: ExitTeamBottariUseCase,
    private val joinTeamBottariUseCase: JoinTeamBottariUseCase,
) : FlowBaseViewModel<MyBottariUiState, MyBottariUiEvent>(MyBottariUiState()) {
    init {
        fetchMyBottaries()
    }

    fun deletePersonalBottari(bottariId: Long) {
        val bottari = currentState.myBottaries.find { bottari -> bottari.id == bottariId } ?: return
        launch {
            updateState { copy(isLoading = true) }
            deleteBottariUseCase(bottariId)
                .onSuccess {
                    cancelAlarm(bottari)
                    emitEvent(MyBottariUiEvent.DeletePersonalBottariSuccess)
                }.onFailure {
                    emitEvent(MyBottariUiEvent.DeletePersonalBottariFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    fun deleteTeamBottari(bottariId: Long) {
        launch {
            updateState { copy(isLoading = true) }
            deleteTeamBottariUseCase(bottariId)
                .onSuccess {
                    fetchTeamBottaries()
                    emitEvent(MyBottariUiEvent.ExitTeamBottariSuccess)
                }.onFailure {
                    emitEvent(MyBottariUiEvent.ExitTeamBottariFailure)
                }
            updateState { copy(isLoading = false) }
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
            updateState { copy(isLoading = true) }
            joinTeamBottariUseCase(code)
                .onSuccess {
                    fetchTeamBottaries()
                }.onFailure {
                    emitEvent(MyBottariUiEvent.JoinTeamBottariFailure)
                }
            updateState { copy(isLoading = false) }
            closeDialog()
        }
    }

    private fun createPersonalBottari(title: String) {
        launch {
            updateState { copy(isLoading = true) }
            createBottariUseCase(title)
                .onSuccess { bottariId ->
                    emitEvent(MyBottariUiEvent.CreatePersonalBottariSuccess(bottariId))
                }.onFailure {
                    emitEvent(MyBottariUiEvent.CreateBottariFailure)
                }
            updateState { copy(isLoading = false) }
            closeDialog()
        }
    }

    private fun createTeamBottari(title: String) {
        launch {
            updateState { copy(isLoading = true) }
            createTeamBottariUseCase(title)
                .onSuccess { bottariId ->
                    bottariId?.let { id ->
                        updateState { copy(isLoading = false) }
                        emitEvent(MyBottariUiEvent.CreateTeamBottariSuccess(id))
                        fetchTeamBottaries()
                    }
                }.onFailure {
                    updateState { copy(isLoading = false) }
                    emitEvent(MyBottariUiEvent.CreateBottariFailure)
                }
            closeDialog()
        }
    }

    private fun fetchMyBottaries() {
        launch {
            fetchPersonalBottaries()
            fetchTeamBottaries()
        }
    }

    private fun cancelAlarm(bottari: MyBottariUiModel) =
        bottari.alarm?.let { alarm ->
            cancelAlarm(
                notification =
                    Notification(
                        bottariId = bottari.id,
                        bottariTitle = bottari.title,
                        alarm = alarm.toDomain(),
                    ),
            )
        }

    private fun fetchTeamBottaries() =
        launch {
            updateState { copy(isLoading = true) }
            fetchTeamBottariesUseCase()
                .onSuccess { bottaries ->
                    updateState {
                        val newBottaries =
                            bottaries.map { bottari ->
                                TeamBottariUiModel.fromDomain(bottari)
                            }
                        copy(
                            teamBottaries = newBottaries,
                        )
                    }
                }.onFailure {
                    emitEvent(MyBottariUiEvent.FetchBottariFailure)
                }
            updateState { copy(isLoading = false) }
        }

    private fun fetchPersonalBottaries() =
        fetchBottariesUseCase()
            .onEach { bottaries ->
                updateState {
                    copy(
                        personalBottaries =
                            bottaries.map { bottari ->
                                BottariUiModel.fromPersonalBottari(bottari)
                            },
                    )
                }
            }.catch {
                emitEvent(MyBottariUiEvent.FetchBottariFailure)
            }.launchIn(viewModelScope)

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MyBottariViewModel(
                        fetchBottariesUseCase = BottariUseCaseProvider.fetchBottariesUseCase,
                        fetchTeamBottariesUseCase = TeamBottariUseCaseProvider.fetchTeamBottariesUseCase,
                        createBottariUseCase = BottariUseCaseProvider.createBottariUseCase,
                        createTeamBottariUseCase = TeamBottariUseCaseProvider.createTeamBottariUseCase,
                        deleteBottariUseCase = BottariUseCaseProvider.deleteBottariUseCase,
                        deleteTeamBottariUseCase = TeamBottariUseCaseProvider.exitTeamBottariUseCase,
                        joinTeamBottariUseCase = TeamMemberUseCaseProvider.joinTeamBottariUseCase,
                    )
                }
            }
    }
}
