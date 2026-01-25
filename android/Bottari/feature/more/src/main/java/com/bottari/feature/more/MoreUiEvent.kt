package com.bottari.feature.more

sealed interface MoreUiEvent {
    data object FetchMemberInfoFailure : MoreUiEvent

    data object SaveMemberNicknameFailure : MoreUiEvent

    data object SaveMemberNicknameSuccess : MoreUiEvent

    data object InvalidNicknameRule : MoreUiEvent
}
