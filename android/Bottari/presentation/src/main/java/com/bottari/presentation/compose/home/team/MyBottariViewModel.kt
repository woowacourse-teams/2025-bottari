package com.bottari.presentation.compose.home.team

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.di.usecase.TeamBottariUseCaseProvider
import com.bottari.di.usecase.TeamMemberUseCaseProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.DeleteBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.domain.usecase.team.ExitTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.domain.usecase.team.JoinTeamBottariUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
        launch {
            deleteBottariUseCase(bottariId)
                .onSuccess {
                    fetchPersonalBottaries()
                    emitEvent(MyBottariUiEvent.DeletePersonalBottariSuccess)
                }.onFailure {
                    emitEvent(MyBottariUiEvent.DeletePersonalBottariFailure)
                }
        }
    }

    fun deleteTeamBottari(bottariId: Long) {
        launch {
            deleteTeamBottariUseCase(bottariId)
                .onSuccess {
                    fetchTeamBottaries()
                    emitEvent(MyBottariUiEvent.ExitTeamBottariSuccess)
                }.onFailure {
                    emitEvent(MyBottariUiEvent.ExitTeamBottariFailure)
                }
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
            joinTeamBottariUseCase(code)
                .onSuccess {
                    fetchTeamBottaries()
                }.onFailure {
                    emitEvent(MyBottariUiEvent.JoinTeamBottariFailure)
                }
            closeDialog()
        }
    }

    private fun createPersonalBottari(title: String) {
        launch {
            createBottariUseCase(title)
                .onSuccess { bottariId ->
                    emitEvent(MyBottariUiEvent.CreatePersonalBottariSuccess(bottariId))
                }.onFailure {
                    emitEvent(MyBottariUiEvent.CreatePersonalBottariFailure)
                }
            closeDialog()
        }
    }

    private fun createTeamBottari(title: String) {
        launch {
            createTeamBottariUseCase(title)
                .onSuccess { bottariId ->
                    bottariId?.let { id ->
                        emitEvent(MyBottariUiEvent.CreateTeamBottariSuccess(id))
                    }
                }.onFailure {
                    emitEvent(MyBottariUiEvent.CreateTeamBottariFailure)
                }
            closeDialog()
        }
    }

    private fun fetchMyBottaries() {
        updateState { copy(isLoading = true) }
        launch {
            val teamBottariesJob = fetchTeamBottaries()
            fetchPersonalBottariesOnce()
            teamBottariesJob.join()
            updateState { copy(isLoading = false) }
            fetchPersonalBottaries()
        }
    }

    private suspend fun fetchPersonalBottariesOnce() {
        val bottaries =
            fetchBottariesUseCase()
                .catch {
                    emitEvent(MyBottariUiEvent.FetchPersonalBottariFailure)
                    emit(emptyList())
                }.firstOrNull()
                ?: return

        updateState {
            copy(personalBottaries = bottaries.map { BottariUiModel.fromPersonalBottari(it) })
        }
    }

    private fun fetchTeamBottaries(): Job =
        viewModelScope.launch {
            fetchTeamBottariesUseCase()
                .onSuccess { bottaries ->
                    updateState {
                        copy(
                            teamBottaries =
                                bottaries.map { bottari ->
                                    TeamBottariUiModel.fromDomain(bottari)
                                },
                        )
                    }
                }.onFailure { emitEvent(MyBottariUiEvent.FetchTeamBottariFailure) }
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
                emitEvent(MyBottariUiEvent.FetchPersonalBottariFailure)
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
