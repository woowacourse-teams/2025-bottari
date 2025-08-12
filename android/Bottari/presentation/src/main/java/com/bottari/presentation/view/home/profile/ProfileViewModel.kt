package com.bottari.presentation.view.home.profile

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.SaveMemberNicknameUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel

class ProfileViewModel(
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val saveMemberNicknameUseCase: SaveMemberNicknameUseCase,
) : BaseViewModel<ProfileUiState, ProfileUiEvent>(ProfileUiState()) {
    init {
        fetchMemberInfo()
    }

    fun updateNickname(nickname: String) {
        updateState { copy(editingNickname = nickname) }
    }

    fun saveNickname() {
        if (currentState.isNicknameChanged.not()) return
        val editingNickname = currentState.editingNickname

        launch {
            saveMemberNicknameUseCase(editingNickname)
                .onSuccess {
                    BottariLogger.ui(
                        UiEventType.NICKNAME_EDIT,
                        mapOf(
                            "old_nickname" to currentState.nickname,
                            "new_nickname" to editingNickname,
                        ),
                    )
                    updateState { copy(nickname = editingNickname) }
                    emitEvent(ProfileUiEvent.SaveMemberNicknameSuccess)
                }.onFailure { error ->
                    updateState { copy(editingNickname = this.nickname) }
                    emitEvent(
                        when (error) {
                            is IllegalArgumentException -> ProfileUiEvent.InvalidNicknameRule
                            else -> ProfileUiEvent.SaveMemberNicknameFailure
                        },
                    )
                }
        }
    }

    private fun fetchMemberInfo() {
        updateState { copy(isLoading = true) }

        launch {
            checkRegisteredMemberUseCase()
                .onSuccess {
                    updateState {
                        copy(
                            nickname = it.name.orEmpty(),
                            editingNickname = it.name.orEmpty(),
                        )
                    }
                }.onFailure { emitEvent(ProfileUiEvent.FetchMemberInfoFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ProfileViewModel(
                        UseCaseProvider.checkRegisteredMemberUseCase,
                        UseCaseProvider.saveMemberNicknameUseCase,
                    )
                }
            }
    }
}
