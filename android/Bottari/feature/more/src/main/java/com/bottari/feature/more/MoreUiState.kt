package com.bottari.feature.more

data class MoreUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val editingNickname: String = "",
) {
    val isNicknameChanged: Boolean = nickname != editingNickname
}
