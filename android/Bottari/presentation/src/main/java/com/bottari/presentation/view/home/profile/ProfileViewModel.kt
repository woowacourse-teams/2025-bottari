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
import com.bottari.presentation.base.UiState
import kotlinx.coroutines.launch

class ProfileViewModel(
    stateHandle: SavedStateHandle,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
) : ViewModel() {
    private val _nickname: MutableLiveData<UiState<String>> = MutableLiveData(UiState.Loading)
    val nickname: LiveData<UiState<String>> = _nickname

    init {
        val ssaid = stateHandle.get<String>(EXTRA_SSAID) ?: error(ERROR_REQUIRED_SSAID)
        fetchNickname(ssaid)
    }

    private fun fetchNickname(ssaid: String) {
        _nickname.value = UiState.Loading

        viewModelScope.launch {
            checkRegisteredMemberUseCase(ssaid)
                .onSuccess { _nickname.value = UiState.Success(it.name ?: "") }
                .onFailure { _nickname.value = UiState.Failure(it.message) }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "EXTRA_SSAID"
        private const val ERROR_REQUIRED_SSAID = "[ERROR] SSAID를 찾지 못함"

        fun Factory(ssaid: String): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_SSAID] = ssaid

                    ProfileViewModel(
                        stateHandle,
                        UseCaseProvider.checkRegisteredMemberUseCase,
                    )
                }
            }
    }
}
