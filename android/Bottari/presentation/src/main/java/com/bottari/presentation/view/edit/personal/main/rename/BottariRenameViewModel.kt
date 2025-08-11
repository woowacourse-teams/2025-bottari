import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.logger.BottariLogger
import com.bottari.logger.model.UiEventType
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameUiEvent
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameUiState

class BottariRenameViewModel(
    savedStateHandle: SavedStateHandle,
    private val saveBottariTitleUseCase: SaveBottariTitleUseCase,
) : BaseViewModel<BottariRenameUiState, BottariRenameUiEvent>(
        BottariRenameUiState(
            initialTitle = savedStateHandle[KEY_INITIAL_TITLE] ?: error(ERROR_REQUIRE_OLD_TITLE),
            title = savedStateHandle[KEY_INITIAL_TITLE] ?: error(ERROR_REQUIRE_OLD_TITLE),
        ),
    ) {
    private val bottariId: Long = savedStateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_NEW_TITLE)

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
        private const val KEY_INITIAL_TITLE = "KEY_INITIAL_TITLE"
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_OLD_TITLE = "[ERROR] 보따리 이름이 없습니다"
        private const val ERROR_REQUIRE_NEW_TITLE = "[ERROR] 보따리 ID가 없습니다"

        fun Factory(
            bottariId: Long,
            initialTitle: String,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val handle = createSavedStateHandle()
                    handle[KEY_INITIAL_TITLE] = initialTitle
                    handle[KEY_BOTTARI_ID] = bottariId

                    BottariRenameViewModel(
                        handle,
                        UseCaseProvider.SaveBottariTitleUseCase,
                    )
                }
            }
    }
}
