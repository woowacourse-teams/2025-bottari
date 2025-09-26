package com.bottari.presentation.compose.home.team

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.BottariUseCaseProvider
import com.bottari.di.usecase.TeamBottariUseCaseProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.domain.usecase.bottari.FetchBottariesUseCase
import com.bottari.domain.usecase.team.CreateTeamBottariUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariesUseCase
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.bottari.presentation.model.bottari.personal.BottariUiModel
import com.bottari.presentation.model.bottari.team.TeamBottariUiModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class MyBottariViewModel(
    private val fetchBottariesUseCase: FetchBottariesUseCase,
    private val fetchTeamBottariesUseCase: FetchTeamBottariesUseCase,
    private val createBottariUseCase: CreateBottariUseCase,
    private val createTeamBottariUseCase: CreateTeamBottariUseCase,
) : FlowBaseViewModel<MyBottariUiState, MyBottariUiEvent>(MyBottariUiState()) {
    init {
        fetchMyBottaries()
    }

    fun createPersonalBottari(title: String) {
        launch {
            createBottariUseCase(title).onSuccess { bottariId ->
                updateState { copy(showPersonalDialog = false) }
                emitEvent(MyBottariUiEvent.CreatePersonalBottariSuccess(bottariId))
            }
        }
    }

    fun createTeamBottari(title: String) {
        launch {
            createTeamBottariUseCase(title).onSuccess { bottariId ->
                bottariId?.let {
                    updateState { copy(showTeamDialog = false) }
                    emitEvent(MyBottariUiEvent.CreateTeamBottariSuccess(it))
                }
            }
        }
    }

    fun openPersonalDialog() {
        updateState { copy(showPersonalDialog = true) }
    }

    fun openTeamDialog() {
        updateState { copy(showTeamDialog = true) }
    }

    fun closePersonalDialog() {
        updateState { copy(showPersonalDialog = false) }
    }

    fun closeTeamDialog() {
        updateState { copy(showTeamDialog = false) }
    }

    private fun fetchMyBottaries() {
        updateState { copy(isLoading = true) }
        viewModelScope.launch {
            val personalBottariesJob = fetchPersonalBottaries()
            val teamBottariesJob = fetchTeamBottaries()
            joinAll(personalBottariesJob, teamBottariesJob)
            updateState { copy(isLoading = false) }
        }
    }

    private fun fetchTeamBottaries() =
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
                }.onFailure { emitEvent(MyBottariUiEvent.TeamBottariFetchFailure) }
        }

    private fun fetchPersonalBottaries() =
        fetchBottariesUseCase()
            .onEach { bottaris ->
                updateState {
                    copy(
                        personalBottaries =
                            bottaris.map { bottari ->
                                BottariUiModel.fromPersonalBottari(bottari)
                            },
                    )
                }
            }.catch {
                emitEvent(MyBottariUiEvent.PersonalBottariFetchFailure)
            }.launchIn(viewModelScope)

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MyBottariViewModel(
                        BottariUseCaseProvider.fetchBottariesUseCase,
                        TeamBottariUseCaseProvider.fetchTeamBottariesUseCase,
                        BottariUseCaseProvider.createBottariUseCase,
                        TeamBottariUseCaseProvider.createTeamBottariUseCase,
                    )
                }
            }
    }
}
