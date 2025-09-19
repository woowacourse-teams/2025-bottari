package com.bottari.presentation.view.home.more

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.MemberUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.SaveMemberNicknameUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel

class MoreViewModel(
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val saveMemberNicknameUseCase: SaveMemberNicknameUseCase,
) : BaseViewModel<MoreUiState, MoreUiEvent>(MoreUiState()) {
    init {
        fetchMemberInfo()
    }

    fun updateNickname(nickname: String) {
        updateState { copy(editingNickname = nickname) }
    }

    fun saveNickname() {
        if (currentState.isNicknameChanged.not()) return
        if (currentState.isLoading) return
        val editingNickname = currentState.editingNickname
        updateState { copy(isLoading = true) }
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
                    emitEvent(MoreUiEvent.SaveMemberNicknameSuccess)
                }.onApiException { bottariException ->
                    updateState { copy(editingNickname = this.nickname) }
                    when (bottariException) {
                        BottariException.InvalidException -> emitEvent(MoreUiEvent.SaveMemberNicknameFailure.InvalidException)
                        BottariException.NotFoundException -> emitEvent(MoreUiEvent.SaveMemberNicknameFailure.NotFoundException)
                        BottariException.DuplicatedException -> emitEvent(MoreUiEvent.SaveMemberNicknameFailure.DuplicatedException)
                        else -> emitEvent(MoreUiEvent.SaveMemberNicknameFailure.UnexpectedException)
                    }
                }.onApiError {
                    updateState { copy(editingNickname = this.nickname) }
                    emitEvent(MoreUiEvent.SaveMemberNicknameFailure.UnexpectedException)
                }
            updateState { copy(isLoading = false) }
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
                }.onApiError { emitEvent(MoreUiEvent.FetchMemberInfoFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MoreViewModel(
                        MemberUseCaseProvider.checkRegisteredMemberUseCase,
                        MemberUseCaseProvider.saveMemberNicknameUseCase,
                    )
                }
            }
    }
}
