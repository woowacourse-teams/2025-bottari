package com.bottari.presentation.view.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.presentation.common.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val savePermissionFlagUseCase: SavePermissionFlagUseCase,
    private val getPermissionFlagUseCase: GetPermissionFlagUseCase,
) : BaseViewModel<MainUiState, MainUiEvent>(MainUiState()) {
    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    init {
        checkPermissionFlag()
    }

    fun checkRegisteredMember() {
        updateState { copy(isLoading = true) }

        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess { result ->
                    handleCheckRegistrationResult(result)
                }.onFailure { emitEvent(MainUiEvent.LoginFailure) }
        }
    }

    fun savePermissionFlag() {
        viewModelScope.launch {
            savePermissionFlagUseCase(true)
                .onFailure { emitEvent(MainUiEvent.SavePermissionFlagFailure) }
        }
    }

    private fun checkPermissionFlag() {
        viewModelScope.launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag -> handlePermissionFlag(permissionFlag) }
                .onFailure { emitEvent(MainUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun handleCheckRegistrationResult(result: RegisteredMember) {
        if (result.isRegistered) {
            updateState { copy(isLoading = false) }
            emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
            return
        }
        registerMember(ssaid)
    }

    private fun handlePermissionFlag(permissionFlag: Boolean) {
        updateState { copy(hasPermissionFlag = permissionFlag) }
        if (!permissionFlag) {
            emitEvent(MainUiEvent.IncompletePermissionFlow)
            return
        }
        checkRegisteredMember()
    }

    private fun registerMember(ssaid: String) {
        viewModelScope.launch {
            registerMemberUseCase(ssaid)
                .onSuccess {
                    updateState { copy(isLoading = false) }
                    emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
                }.onFailure { emitEvent(MainUiEvent.RegisterFailure) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID가 존재하지 않습니다."

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
                    MainViewModel(
                        stateHandle,
                        UseCaseProvider.registerMemberUseCase,
                        UseCaseProvider.checkRegisteredMemberUseCase,
                        UseCaseProvider.savePermissionFlagUseCase,
                        UseCaseProvider.getPermissionFlagUseCase,
                    )
                }
            }
    }
}
