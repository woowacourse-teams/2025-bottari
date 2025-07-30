import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.SaveBottariTitleUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.update
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameUiEvent
import com.bottari.presentation.view.edit.personal.main.rename.BottariRenameUiState
import kotlinx.coroutines.launch

class BottariRenameViewModel(
    savedStateHandle: SavedStateHandle,
    private val saveBottariTitleUseCase: SaveBottariTitleUseCase,
) : ViewModel() {
    private val ssaid: String =
        savedStateHandle.get<String>(KEY_SSAID) ?: error(ERROR_REQUIRE_SSAID)
    private val id: Long =
        savedStateHandle.get<Long>(KEY_BOTTARI_ID) ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val oldTitle: String =
        savedStateHandle.get<String>(KEY_OLD_TITLE) ?: error(ERROR_REQUIRE_OLD_TITLE)

    private val _uiState: MutableLiveData<BottariRenameUiState> =
        MutableLiveData(BottariRenameUiState())
    val uiState: LiveData<BottariRenameUiState> = _uiState

    private val _uiEvent: SingleLiveEvent<BottariRenameUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<BottariRenameUiEvent> = _uiEvent

    fun saveBottariTitle(newTitle: String) {
        if (!isValidTitle(newTitle)) return

        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            saveBottariTitleUseCase(id, ssaid, newTitle)
                .onSuccess {
                    _uiState.update { copy(title = newTitle) }
                    _uiEvent.value = BottariRenameUiEvent.SaveBottariTitleSuccess
                }
                .onFailure {
                    _uiEvent.value = BottariRenameUiEvent.SaveBottariTitleFailure
                }
            _uiState.update { copy(isLoading = false) }
        }
    }

    fun cacheTitleInput(newTitle: String) {
        _uiState.update { copy(title = newTitle) }
    }

    private fun isValidTitle(newTitle: String): Boolean =
        when {
            newTitle.isBlank() -> false
            newTitle == oldTitle -> {
                _uiEvent.value = BottariRenameUiEvent.SaveBottariTitleSuccess
                false
            }

            else -> true
        }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val KEY_OLD_TITLE = "KEY_OLD_TITLE"
        private const val KEY_SSAID = "KEY_SSAID"

        private const val ERROR_REQUIRE_BOTTARI_ID = "보따리 ID가 없습니다"
        private const val ERROR_REQUIRE_OLD_TITLE = "보따리 이름이 없습니다"
        private const val ERROR_REQUIRE_SSAID = "SSAID가 없습니다"

        fun Factory(
            ssaid: String,
            bottariId: Long,
            oldTitle: String,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val handle = createSavedStateHandle()
                    handle[KEY_BOTTARI_ID] = bottariId
                    handle[KEY_OLD_TITLE] = oldTitle
                    handle[KEY_SSAID] = ssaid

                    BottariRenameViewModel(
                        handle,
                        UseCaseProvider.renameBottariUseCase,
                    )
                }
            }
    }
}
