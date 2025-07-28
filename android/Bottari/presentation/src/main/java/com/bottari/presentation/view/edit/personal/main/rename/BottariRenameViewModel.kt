import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.RenameBottariUseCase
import com.bottari.presentation.base.UiState
import kotlinx.coroutines.launch

class BottariRenameViewModel(
    savedStateHandle: SavedStateHandle,
    private val renameBottariUseCase: RenameBottariUseCase,
) : ViewModel() {
    private val ssaid: String = savedStateHandle.get<String>(KEY_SSAID) ?: error(ERROR_REQUIRE_SSAID)
    private val id: Long = savedStateHandle.get<Long>(KEY_BOTTARI_ID) ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val oldTitle: String = savedStateHandle.get<String>(KEY_OLD_TITLE) ?: error(ERROR_REQUIRE_OLD_TITLE)

    private val _renameSuccess = MutableLiveData<UiState<Unit?>>()
    val renameSuccess: MutableLiveData<UiState<Unit?>> = _renameSuccess

    fun renameBottari(newTitle: String) {
        if (!isValidTitle(newTitle)) return

        _renameSuccess.value = UiState.Loading

        viewModelScope.launch {
            renameBottariUseCase(id, ssaid, newTitle)
                .onSuccess { _renameSuccess.value = UiState.Success(it) }
                .onFailure { _renameSuccess.value = UiState.Failure(it.message) }
        }
    }

    private fun isValidTitle(newTitle: String): Boolean =
        when {
            newTitle.isBlank() -> false
            newTitle == oldTitle -> {
                _renameSuccess.value = UiState.Success(Unit)
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
