package com.bottari.bottari.view

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
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.presentation.base.UiState
import kotlinx.coroutines.launch

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
) : ViewModel() {
    private val ssaid = stateHandle.get<String>(EXTRA_SSAID) ?: error(NOT_FOUND_USER)
    private val _loginState: MutableLiveData<UiState<Unit>> = MutableLiveData(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> = _loginState

    init {
        checkRegisteredMember()
    }

    private fun checkRegisteredMember() {
        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess { handleCheckRegistrationResult(it) }
                .onFailure { _loginState.value = UiState.Failure(it.message) }
        }
    }

    private fun handleCheckRegistrationResult(result: RegisteredMember) {
        if (result.isRegistered) {
            _loginState.value = UiState.Success(Unit)
            return
        }

        registerMember(ssaid)
    }

    private fun registerMember(ssaid: String) {
        viewModelScope.launch {
            registerMemberUseCase(ssaid)
                .onFailure { _loginState.value = UiState.Failure(it.message) }
                .onSuccess { _loginState.value = UiState.Success(Unit) }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "SSAID"
        private const val NOT_FOUND_USER = "SSAID를 확인할 수 없음"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_SSAID] = ssaid
                    MainViewModel(
                        stateHandle,
                        UseCaseProvider.registerMemberUseCase,
                        UseCaseProvider.checkRegisteredMemberUseCase,
                    )
                }
            }
    }
}
