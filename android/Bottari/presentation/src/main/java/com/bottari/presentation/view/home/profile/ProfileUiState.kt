package com.bottari.presentation.view.home.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val editingNickname: String = "",
) {
    val isNicknameChanged: Boolean get() = nickname != editingNickname
}
