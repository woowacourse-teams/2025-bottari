package com.bottari.feature.more

import androidx.compose.runtime.Stable
import com.bottari.core.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.core.domain.usecase.member.SaveMemberNicknameUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Stable
@HiltViewModel
class MoreViewModel @Inject constructor(
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

        updateState { copy(isLoading = true) }
        val editingNickname = currentState.editingNickname

        launch {
            saveMemberNicknameUseCase(editingNickname)
                .onSuccess {
                    updateState { copy(nickname = editingNickname) }
                    emitEvent(MoreUiEvent.SaveMemberNicknameSuccess)
                    logSaveNickname(editingNickname)
                }.onFailure { error ->
                    updateState { copy(editingNickname = this.nickname) }
                    emitEvent(
                        when (error) {
                            is IllegalArgumentException -> MoreUiEvent.InvalidNicknameRule
                            else -> MoreUiEvent.SaveMemberNicknameFailure
                        },
                    )
                }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
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
                }.onFailure { emitEvent(MoreUiEvent.FetchMemberInfoFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun logSaveNickname(editingNickname: String) {
        BottariLogger.ui(
            UiEventType.NICKNAME_EDIT,
            mapOf(
                "old_nickname" to currentState.nickname,
                "new_nickname" to editingNickname,
            ),
        )
    }
}
