package com.bottari.presentation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import kotlinx.coroutines.launch

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val savePermissionFlagUseCase: SavePermissionFlagUseCase,
    private val getPermissionFlagUseCase: GetPermissionFlagUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<MainUiState> = MutableLiveData(MainUiState())
    val uiState: LiveData<MainUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<MainUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<MainUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    init {
        checkPermissionFlag()
    }

    fun checkRegisteredMember() {
        _uiState.update { copy(isLoading = true) }
        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess { result ->
                    handleCheckRegistrationResult(result)
                }.onFailure { _uiEvent.value = MainUiEvent.LoginFailure }
        }
    }

    fun savePermissionFlag() {
        viewModelScope.launch {
            savePermissionFlagUseCase(true)
                .onFailure { _uiEvent.value = MainUiEvent.SavePermissionFlagFailure }
        }
    }

    private fun checkPermissionFlag() {
        viewModelScope.launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag -> handlePermissionFlag(permissionFlag) }
                .onFailure { _uiEvent.value = MainUiEvent.GetPermissionFlagFailure }
        }
    }

    private fun handleCheckRegistrationResult(result: RegisteredMember) {
        if (result.isRegistered) {
            _uiState.update { copy(isLoading = false, isReady = true) }
            _uiEvent.value = MainUiEvent.LoginSuccess(_uiState.value!!.hasPermissionFlag)
            return
        }
        registerMember(ssaid)
    }

    private fun handlePermissionFlag(permissionFlag: Boolean) {
        _uiState.update { copy(hasPermissionFlag = permissionFlag) }
        if (!permissionFlag) {
            _uiEvent.value = MainUiEvent.IncompletePermissionFlow
            return
        }
        checkRegisteredMember()
    }

    private fun registerMember(ssaid: String) {
        viewModelScope.launch {
            registerMemberUseCase(ssaid)
                .onSuccess {
                    _uiState.update { copy(isLoading = false, isReady = true) }
                    _uiEvent.value = MainUiEvent.LoginSuccess(_uiState.value!!.hasPermissionFlag)
                }.onFailure { _uiEvent.value = MainUiEvent.RegisterFailure }
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
