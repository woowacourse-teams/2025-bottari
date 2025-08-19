package com.bottari.presentation.view.join

data class TeamBottariJoinUiState(
    val inviteCode: String = "",
) {
    val isCanJoin: Boolean = inviteCode.trim().isNotBlank()
}
