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
import com.bottari.domain.model.member.Member
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.UpdateMemberNicknameUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.extension.takeSuccess
import kotlinx.coroutines.launch

class ProfileViewModel(
    stateHandle: SavedStateHandle,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val updateMemberNicknameUseCase: UpdateMemberNicknameUseCase,
) : ViewModel() {
    private val _nickname: MutableLiveData<UiState<String>> = MutableLiveData(UiState.Loading)
    val nickname: LiveData<UiState<String>> = _nickname

    private val ssaid: String by lazy {
        stateHandle.get<String>(KEY_SSAID) ?: error(
            ERROR_REQUIRED_SSAID,
        )
    }

    init {
        fetchNickname(ssaid)
    }

    fun updateNickName(nickname: String) {
        val currentNickname = _nickname.value?.takeSuccess().orEmpty()
        runCatching { Member(ssaid, nickname) }
            .onSuccess { member ->
                viewModelScope.launch {
                    updateMemberNicknameUseCase(member)
                        .onSuccess { _nickname.value = UiState.Success(nickname) }
                        .onFailure { _nickname.value = UiState.Failure(currentNickname) }
                }
            }.onFailure {
                _nickname.value = UiState.Failure(currentNickname)
            }
    }

    private fun fetchNickname(ssaid: String) {
        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess { _nickname.value = UiState.Success(it.name ?: "") }
                .onFailure { _nickname.value = UiState.Failure(it.message) }
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
                        UseCaseProvider.updateMemberNicknameUseCase,
                    )
                }
            }
    }
}
