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
import com.bottari.domain.usecase.alarm.ToggleAlarmStateUseCase
import com.bottari.domain.usecase.bottariDetail.FindBottariDetailUseCase
import com.bottari.domain.usecase.template.CreateBottariTemplateUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.extension.update
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditUiEvent
import com.bottari.presentation.view.edit.personal.main.PersonalBottariEditUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PersonalBottariEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val findBottariDetailUseCase: FindBottariDetailUseCase,
    private val toggleAlarmStateUseCase: ToggleAlarmStateUseCase,
    private val createBottariTemplateUseCase: CreateBottariTemplateUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<PersonalBottariEditUiState> =
        MutableLiveData(PersonalBottariEditUiState())
    val uiState: LiveData<PersonalBottariEditUiState> get() = _uiState

    private val _uiEvent = SingleLiveEvent<PersonalBottariEditUiEvent>()
    val uiEvent: LiveData<PersonalBottariEditUiEvent> get() = _uiEvent

    private val ssaid: String =
        savedStateHandle.get<String>(EXTRA_SSAID) ?: error(ERROR_SSAID_MISSING)

    private val bottariId: Long =
        savedStateHandle.get<Long>(EXTRA_BOTTARI_ID) ?: error(ERROR_BOTTARI_ID_MISSING)

    private var toggleAlarmJob: Job? = null

    init {
        fetchBottari()
    }

    fun toggleAlarmState(isActive: Boolean) {
        val bottari = _uiState.value?.bottari ?: return
        val alarmId = bottari.alarm?.id ?: return
        toggleAlarmJob?.cancel()
        toggleAlarmJob =
            viewModelScope.launch {
                delay(DEBOUNCE_DELAY)
                toggleAlarmStateUseCase(ssaid, alarmId, isActive)
            }
    }

    fun createBottariTemplate() {
        _uiState.update { copy(isLoading = true) }

        val bottari = _uiState.value?.bottari ?: return
        val title = bottari.title
        val items = bottari.items.map { it.name }

        viewModelScope.launch {
            createBottariTemplateUseCase(ssaid, title, items)
                .onSuccess {
                    _uiEvent.value = PersonalBottariEditUiEvent.CreateTemplateSuccess
                }.onFailure {
                    _uiEvent.value = PersonalBottariEditUiEvent.CreateTemplateFailure
                }
            _uiState.update { copy(isLoading = false) }
        }
    }

    fun fetchBottari() {
        _uiState.update { copy(isLoading = true) }

        viewModelScope.launch {
            findBottariDetailUseCase(bottariId, ssaid)
                .onSuccess { _uiState.update { copy(bottari = it.toUiModel()) } }
                .onFailure {
                    _uiEvent.value = PersonalBottariEditUiEvent.FetchBottariFailure
                }
            _uiState.update { copy(isLoading = false) }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "EXTRA_SSAID"
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        private const val DEBOUNCE_DELAY = 500L

        private const val ERROR_SSAID_MISSING = "SSAID를 확인할 수 없습니다"
        private const val ERROR_BOTTARI_ID_MISSING = "보따리 Id가 없습니다"

        fun Factory(
            ssaid: String,
            bottariId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[EXTRA_SSAID] = ssaid
                    stateHandle[EXTRA_BOTTARI_ID] = bottariId

                    PersonalBottariEditViewModel(
                        stateHandle,
                        UseCaseProvider.findBottariDetailUseCase,
                        UseCaseProvider.toggleAlarmStateUseCase,
                        UseCaseProvider.createBottariTemplateUseCase,
                    )
                }
            }
    }
}
