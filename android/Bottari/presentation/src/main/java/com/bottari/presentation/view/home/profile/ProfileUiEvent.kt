package com.bottari.presentation.view.home.profile

sealed interface ProfileUiEvent {
    data object FetchMemberInfoFailure : ProfileUiEvent

    data object SaveMemberNicknameFailure : ProfileUiEvent

    data object SaveMemberNicknameSuccess : ProfileUiEvent

    data object InvalidNicknameRule : ProfileUiEvent
}
