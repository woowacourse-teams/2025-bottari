package com.bottari.presentation.view.home.profile

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
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.SaveMemberNicknameUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.takeSuccess
import kotlinx.coroutines.launch

class ProfileViewModel(
    stateHandle: SavedStateHandle,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val saveMemberNicknameUseCase: SaveMemberNicknameUseCase,
) : ViewModel() {
    private val _nickname: MutableLiveData<UiState<String>> = MutableLiveData(UiState.Loading)
    val nickname: LiveData<UiState<String>> = _nickname

    private val _nicknameEvent = SingleLiveEvent<String>()
    val nicknameEvent: SingleLiveEvent<String> get() = _nicknameEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRED_SSAID)

    init {
        fetchNickname(ssaid)
    }

    fun saveNickname(nickname: String) {
        val currentNickname = _nickname.value?.takeSuccess() ?: ""
        viewModelScope.launch {
            saveMemberNicknameUseCase(ssaid, nickname)
                .onSuccess {
                    _nickname.value = UiState.Success(nickname)
                }.onFailure { error ->
                    _nickname.value = UiState.Failure(currentNickname)
                    _nicknameEvent.emit(error.message)
                }
        }
    }

    private fun fetchNickname(ssaid: String) {
        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess { _nickname.value = UiState.Success(it.name ?: "") }
                .onFailure { error -> _nicknameEvent.emit(error.message) }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val ERROR_REQUIRED_SSAID = "[ERROR] SSAID를 찾지 못함"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid

                    ProfileViewModel(
                        stateHandle,
                        UseCaseProvider.checkRegisteredMemberUseCase,
                        UseCaseProvider.saveMemberNicknameUseCase,
                    )
                }
            }
    }
}
