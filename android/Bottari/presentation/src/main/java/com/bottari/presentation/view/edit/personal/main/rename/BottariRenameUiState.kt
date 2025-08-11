package com.bottari.presentation.view.edit.personal.main.rename

data class BottariRenameUiState(
    val isLoading: Boolean = false,
    val initialTitle: String = "",
    val title: String = "",
) {
    val isSaveEnabled: Boolean get() = title.isNotBlank() && title != initialTitle
}
