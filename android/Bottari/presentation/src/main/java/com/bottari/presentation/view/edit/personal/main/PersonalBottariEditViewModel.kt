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
import com.bottari.presentation.base.UiState
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariDetailUiModel
import kotlinx.coroutines.launch

class PersonalBottariEditViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val findBottariDetailUseCase: FindBottariDetailUseCase,
    private val toggleAlarmStateUseCase: ToggleAlarmStateUseCase,
) : ViewModel() {
    private val _bottari = MutableLiveData<UiState<BottariDetailUiModel>>()
    val bottari: LiveData<UiState<BottariDetailUiModel>> = _bottari

    private val ssaid: String =
        savedStateHandle.get<String>(EXTRA_SSAID) ?: error(ERROR_SSAID_MISSING)

    private val bottariId: Long =
        savedStateHandle.get<Long>(EXTRA_BOTTARI_ID) ?: error(ERROR_BOTTARI_ID_MISSING)

    init {
        fetchBottariById(bottariId)
    }

    fun toggleAlarmState(isActive: Boolean) {
        val currentState = _bottari.value
        if (currentState is UiState.Success) {
            val alarmId = currentState.data.alarm?.id
            if (alarmId != null) {
                viewModelScope.launch {
                    toggleAlarmStateUseCase.invoke(ssaid, alarmId, isActive)
                }
            } else {
                throw IllegalArgumentException(ERROR_ALARM_ID_MISSING)
            }
        }
    }

    private fun fetchBottariById(id: Long) {
        viewModelScope.launch {
            _bottari.value = UiState.Loading
            findBottariDetailUseCase
                .invoke(id, ssaid)
                .onSuccess { _bottari.value = UiState.Success(it.toUiModel()) }
                .onFailure { _bottari.value = UiState.Failure(it.message ?: ERROR_UNKNOWN) }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "SSAID"
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"

        private const val ERROR_SSAID_MISSING = "SSAID를 확인할 수 없습니다"
        private const val ERROR_BOTTARI_ID_MISSING = "보따리 Id가 없습니다"
        private const val ERROR_ALARM_ID_MISSING = "알람 ID가 없습니다"
        private const val ERROR_UNKNOWN = "알 수 없는 오류"

        fun Factory(
            ssaid: String,
            bottariId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val handle =
                        createSavedStateHandle().apply {
                            this[EXTRA_SSAID] = ssaid
                            this[EXTRA_BOTTARI_ID] = bottariId
                        }
                    val findBottariDetailUseCase = UseCaseProvider.findBottariDetailUseCase
                    val toggleAlarmStateUseCase = UseCaseProvider.toggleAlarmStateUseCase

                    PersonalBottariEditViewModel(
                        handle,
                        findBottariDetailUseCase,
                        toggleAlarmStateUseCase,
                    )
                }
            }
    }
}
