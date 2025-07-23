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
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.presentation.base.UiState
import kotlinx.coroutines.launch

class MainViewModel(
    stateHandle: SavedStateHandle,
    private val registerMemberUseCase: RegisterMemberUseCase,
) : ViewModel() {
    private val _loginState: MutableLiveData<UiState<Unit>> = MutableLiveData(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> = _loginState

    init {
        val ssaid = stateHandle.get<String>(EXTRA_SSAID) ?: error(NOT_FOUND_USER)
        checkRegisteredMember(ssaid)
    }

    private fun checkRegisteredMember(ssaid: String) {
        viewModelScope.launch {
            // todo: 유저 등록 여부 확인 필요
            val isUnRegistered = false
            if (isUnRegistered) {
                registerMember(ssaid)
                return@launch
            }

            _loginState.value = UiState.Success(Unit)
        }
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
                    MainViewModel(stateHandle, UseCaseProvider.registerMemberUseCase)
                }
            }
    }
}
