package com.bottari.presentation.view.home.more

data class MoreUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val editingNickname: String = "",
) {
    val isNicknameChanged: Boolean get() = nickname != editingNickname
}
