package com.bottari.presentation.view.home.more

sealed interface MoreUiEvent {
    data object FetchMemberInfoFailure : MoreUiEvent

    sealed interface SaveMemberNicknameFailure : MoreUiEvent {
        data object InvalidException : SaveMemberNicknameFailure

        data object NotFoundException : SaveMemberNicknameFailure

        data object DuplicatedException : SaveMemberNicknameFailure

        data object UnexpectedException : SaveMemberNicknameFailure
    }

    data object SaveMemberNicknameSuccess : MoreUiEvent
}
