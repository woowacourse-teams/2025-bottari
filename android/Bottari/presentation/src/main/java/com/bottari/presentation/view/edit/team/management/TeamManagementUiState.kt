package com.bottari.presentation.view.edit.team.management

data class TeamManagementUiState(
    val isLoading: Boolean = false,
    val inviteCode: String = "",
    val teamMemberHeadCount: Int = DEFAULT_VALUE,
    val maxHeadCount: Int = DEFAULT_VALUE,
    val hostName: String = "",
    val memberNicknames: List<String> = emptyList(),
) {
    val nonDuplicateNicknames: List<String>
        get() = listOf(hostName) + memberNicknames.filter { nickname -> nickname != hostName }

    companion object {
        private const val DEFAULT_VALUE = 0
    }
}
