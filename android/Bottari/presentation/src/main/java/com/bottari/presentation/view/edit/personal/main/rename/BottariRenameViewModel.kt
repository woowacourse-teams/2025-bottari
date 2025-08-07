import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameUiEvent
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameUiState

class BottariRenameViewModel(
    savedStateHandle: SavedStateHandle,
    private val saveBottariTitleUseCase: SaveBottariTitleUseCase,
) : BaseViewModel<BottariRenameUiState, BottariRenameUiEvent>(
        BottariRenameUiState(
            title = savedStateHandle[KEY_INITIAL_TITLE] ?: error(ERROR_REQUIRE_OLD_TITLE),
        ),
    ) {
    private val ssaid: String = savedStateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)

    fun saveBottariTitle(
        bottariId: Long,
        newTitle: String,
    ) {
        if (!isValidTitle(newTitle)) return

        updateState { copy(isLoading = true) }

        launch {
            saveBottariTitleUseCase(bottariId, ssaid, newTitle)
                .onSuccess {
                    updateState { copy(title = newTitle) }
                    emitEvent(BottariRenameUiEvent.SaveBottariTitleSuccess)
                }.onFailure {
                    emitEvent(BottariRenameUiEvent.SaveBottariTitleFailure)
                }
            updateState { copy(isLoading = false) }
        }
    }

    fun cacheTitleInput(newTitle: String) {
        updateState { copy(title = newTitle) }
    }

    private fun isValidTitle(newTitle: String): Boolean =
        when {
            newTitle.isBlank() -> false
            newTitle == currentState.initialTitle -> {
                emitEvent(BottariRenameUiEvent.SaveBottariTitleSuccess)
                false
            }

            else -> true
        }

    companion object {
        private const val KEY_INITIAL_TITLE = "KEY_INITIAL_TITLE"
        private const val KEY_SSAID = "KEY_SSAID"

        private const val ERROR_REQUIRE_OLD_TITLE = "[ERROR] 보따리 이름이 없습니다"
        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID가 없습니다"

        fun Factory(
            ssaid: String,
            initialTitle: String,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val handle = createSavedStateHandle()
                    handle[KEY_INITIAL_TITLE] = initialTitle
                    handle[KEY_SSAID] = ssaid

                    BottariRenameViewModel(
                        handle,
                        UseCaseProvider.SaveBottariTitleUseCase,
                    )
                }
            }
    }
}
