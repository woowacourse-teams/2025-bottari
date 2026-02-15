package com.bottari.feature.personal.edit.rename

import com.bottari.core.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = BottariRenameViewModel.Factory::class)
class BottariRenameViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val saveBottariTitleUseCase: SaveBottariTitleUseCase,
) : BaseViewModel<BottariRenameUiState, BottariRenameUiEvent>(BottariRenameUiState()) {
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

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): BottariRenameViewModel
    }
}
