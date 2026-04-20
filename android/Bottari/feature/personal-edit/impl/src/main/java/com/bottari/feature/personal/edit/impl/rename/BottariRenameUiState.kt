package com.bottari.feature.personal.edit.impl.rename

data class BottariRenameUiState(
    val isLoading: Boolean = false,
    val initialTitle: String = "",
    val title: String = "",
) {
    val isSaveEnabled: Boolean get() = title.isNotBlank() && title != initialTitle
}
