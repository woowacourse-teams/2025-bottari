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
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    stateHandle: SavedStateHandle,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val saveMemberNicknameUseCase: SaveMemberNicknameUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<ProfileUiState> = MutableLiveData(ProfileUiState())
    val uiState: LiveData<ProfileUiState> = _uiState

    private val _uiEvent: SingleLiveEvent<ProfileUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<ProfileUiEvent> = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRED_SSAID)

    init {
        fetchMemberInfo()
    }

    fun updateNickname(nickname: String) {
        _uiState.update { copy(editingNickname = nickname) }
    }

    fun saveNickname() {
        if (_uiState.value?.isNicknameChanged == false) return
        val editingNickname = _uiState.value?.editingNickname.orEmpty()

        viewModelScope.launch {
            saveMemberNicknameUseCase(ssaid, editingNickname)
                .onSuccess {
                    _uiState.update { copy(nickname = editingNickname) }
                    _uiEvent.value = ProfileUiEvent.SaveMemberNicknameSuccess
                }.onFailure { error ->
                    _uiState.update { copy(editingNickname = this.nickname) }
                    _uiEvent.value =
                        when (error) {
                            is IllegalArgumentException -> ProfileUiEvent.InvalidNicknameRule
                            else -> ProfileUiEvent.SaveMemberNicknameFailure
                        }
                }
        }
    }

    private fun fetchMemberInfo() {
        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess {
                    _uiState.update {
                        copy(
                            nickname = it.name.orEmpty(),
                            editingNickname = it.name.orEmpty(),
                        )
                    }
                }.onFailure { _uiEvent.value = ProfileUiEvent.FetchMemberInfoFailure }
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
