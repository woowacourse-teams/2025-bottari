package com.bottari.presentation.compose.edit.personal.rename

data class BottariRenameUiState(
    val isLoading: Boolean = false,
    val initialTitle: String = "",
    val title: String = "",
) {
    val isSaveEnabled: Boolean get() = title.isNotBlank() && title != initialTitle
}
