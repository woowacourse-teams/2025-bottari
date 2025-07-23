package com.bottari.presentation.view.checklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.extension.takeSuccess
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchChecklistUseCase: FetchChecklistUseCase,
    private val checkBottariItemUseCase: CheckBottariItemUseCase,
    private val unCheckBottariItemUseCase: UnCheckBottariItemUseCase,
) : ViewModel() {
    private val ssaid: String by lazy { stateHandle.get<String>(EXTRA_SSAID)!! }
    private val pendingCheckStatusMap = mutableMapOf<Long, Boolean>()
    private var debounceJob: Job? = null

    private val _checklist = MutableLiveData<UiState<List<BottariItemUiModel>>>(UiState.Loading)
    val checklist: LiveData<UiState<List<BottariItemUiModel>>> = _checklist

    private val _nonChecklist = MutableLiveData<List<BottariItemUiModel>>(emptyList())
    val nonChecklist: LiveData<List<BottariItemUiModel>> = _nonChecklist

    val checkedQuantity: LiveData<Int> = _checklist.map { state ->
        state.takeSuccess().orEmpty().count { it.isChecked }
    }

    val isAllChecked: LiveData<Boolean> = _checklist.map { state ->
        state.takeSuccess().orEmpty().all { it.isChecked }
    }

    init {
        val bottariId = stateHandle.get<Long>(EXTRA_BOTTARI_ID)!!
        fetchChecklist(bottariId)
    }

    fun toggleItemChecked(itemId: Long) {
        val updatedList = currentChecklist().map { item ->
            if (item.id != itemId) return@map item
            val toggledChecked = !item.isChecked
            recordPendingCheckStatus(item.id, toggledChecked)
            item.copy(isChecked = toggledChecked)
        }

        _checklist.value = UiState.Success(updatedList)
        restartDebounceTimer()
    }

    fun filterUncheckedItems() {
        _nonChecklist.value = currentChecklist().filter { !it.isChecked }
    }

    private fun currentChecklist(): List<BottariItemUiModel> =
        _checklist.value?.takeSuccess().orEmpty()

    private fun recordPendingCheckStatus(itemId: Long, isChecked: Boolean) {
        pendingCheckStatusMap[itemId] = isChecked
    }

    private fun restartDebounceTimer() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY_MS)
            val copyPendingMap = pendingCheckStatusMap.toMap()
            pendingCheckStatusMap.clear()

            copyPendingMap.forEach { (itemId, isChecked) ->
                launch { updateCheckStatus(itemId, isChecked) }
            }
        }
    }

    private suspend fun updateCheckStatus(itemId: Long, isChecked: Boolean) {
        if (isChecked) {
            checkBottariItemUseCase(ssaid, itemId)
            return
        }

        unCheckBottariItemUseCase(ssaid, itemId)
    }

    private fun fetchChecklist(bottariId: Long) {
        viewModelScope.launch {
            fetchChecklistUseCase(ssaid, bottariId)
                .onSuccess { items ->
                    _checklist.value = UiState.Success(items.map { it.toUiModel() })
                }
                .onFailure { error ->
                    _checklist.value = UiState.Failure(error.message)
                }
        }
    }

    companion object {
        private const val EXTRA_SSAID = "EXTRA_SSAID"
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val DEBOUNCE_DELAY_MS = 500L

        fun Factory(ssaid: String, bottariId: Long): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val stateHandle = createSavedStateHandle()
                stateHandle[EXTRA_SSAID] = ssaid
                stateHandle[EXTRA_BOTTARI_ID] = bottariId
                ChecklistViewModel(
                    stateHandle,
                    UseCaseProvider.fetchChecklistUseCase,
                    UseCaseProvider.checkBottariItemUseCase,
                    UseCaseProvider.unCheckBottariItemUseCase,
                )
            }
        }
    }
}
