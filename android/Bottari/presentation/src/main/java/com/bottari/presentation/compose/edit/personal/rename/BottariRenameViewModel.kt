package com.bottari.presentation.compose.edit.personal.rename

import androidx.lifecycle.SavedStateHandle
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.FlowBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottariRenameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveBottariTitleUseCase: SaveBottariTitleUseCase,
) : FlowBaseViewModel<BottariRenameUiState, BottariRenameUiEvent>(BottariRenameUiState()) {
    private val bottariId: Long = savedStateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)

    fun setInitialTitle(title: String) {
        updateState { copy(initialTitle = title, title = title) }
    }

    fun cacheTitleInput(newTitle: String) {
        updateState { copy(title = newTitle) }
    }

    fun saveBottariTitle() {
        if (!currentState.isSaveEnabled) return

        updateState { copy(isLoading = true) }

        launch {
            saveBottariTitleUseCase(bottariId, currentState.title)
                .onSuccess {
                    BottariLogger.ui(
                        UiEventType.PERSONAL_BOTTARI_TITLE_EDIT,
                        mapOf(
                            "bottari_id" to bottariId.toString(),
                            "old_title" to currentState.initialTitle,
                            "new_title" to currentState.title,
                        ),
                    )
                    emitEvent(BottariRenameUiEvent.SaveBottariTitleSuccess)
                }.onFailure {
                    emitEvent(BottariRenameUiEvent.SaveBottariTitleFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 없습니다"
    }
}
