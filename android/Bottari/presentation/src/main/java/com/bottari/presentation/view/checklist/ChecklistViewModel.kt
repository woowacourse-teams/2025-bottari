package com.bottari.presentation.view.checklist

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
import com.bottari.domain.usecase.item.CheckBottariItemUseCase
import com.bottari.domain.usecase.item.FetchChecklistUseCase
import com.bottari.domain.usecase.item.UnCheckBottariItemUseCase
import com.bottari.presentation.common.event.SingleLiveEvent
import com.bottari.presentation.common.extension.update
import com.bottari.presentation.mapper.BottariMapper.toUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.util.debounce
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class ChecklistViewModel(
    stateHandle: SavedStateHandle,
    private val fetchChecklistUseCase: FetchChecklistUseCase,
    private val checkBottariItemUseCase: CheckBottariItemUseCase,
    private val unCheckBottariItemUseCase: UnCheckBottariItemUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<ChecklistUiState> = MutableLiveData(ChecklistUiState())
    val uiState: LiveData<ChecklistUiState> get() = _uiState

    private val _uiEvent: SingleLiveEvent<ChecklistUiEvent> = SingleLiveEvent()
    val uiEvent: LiveData<ChecklistUiEvent> get() = _uiEvent

    private val ssaid: String = stateHandle[KEY_SSAID] ?: error(ERROR_REQUIRE_SSAID)
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_REQUIRE_BOTTARI_ID)
    private val pendingCheckStatusMap = mutableMapOf<Long, BottariItemUiModel>()
    private val debouncedCheck: (List<BottariItemUiModel>) -> Unit =
        debounce(
            timeMillis = DEBOUNCE_DELAY,
            coroutineScope = viewModelScope,
        ) { items -> performCheck(items) }

    init {
        fetchChecklist(bottariId)
    }

    fun toggleItemChecked(itemId: Long) {
        val currentItems = _uiState.value?.bottariItems ?: return
        val updatedItems =
            currentItems.map { item ->
                if (item.id != itemId) return@map item
                val newItem = item.copy(isChecked = item.isChecked.not())
                recordPendingCheckStatus(newItem)
                newItem
            }
        _uiState.update { copy(bottariItems = updatedItems) }
        debouncedCheck(pendingCheckStatusMap.values.toList())
    }

    private fun performCheck(items: List<BottariItemUiModel>) {
        viewModelScope.launch {
            val deferred =
                items.map { item ->
                    async {
                        val result =
                            if (item.isChecked) {
                                checkBottariItemUseCase(ssaid, item.id)
                            } else {
                                unCheckBottariItemUseCase(ssaid, item.id)
                            }

                        result.onFailure { _uiEvent.value = ChecklistUiEvent.CheckItemFailure }
                    }
                }
            deferred.awaitAll()
            pendingCheckStatusMap.clear()
        }
    }

    private fun recordPendingCheckStatus(item: BottariItemUiModel) {
        pendingCheckStatusMap[item.id] = item
    }

    private fun fetchChecklist(bottariId: Long) {
        _uiState.update { copy(isLoading = true) }
        viewModelScope.launch {
            fetchChecklistUseCase(ssaid, bottariId)
                .onSuccess { items ->
                    val itemUiModels = items.map { it.toUiModel() }
                    _uiState.update { copy(isLoading = false, bottariItems = itemUiModels) }
                }.onFailure {
                    _uiState.update { copy(isLoading = false) }
                    _uiEvent.value = ChecklistUiEvent.FetchChecklistFailure
                }
        }
    }

    companion object {
        private const val KEY_SSAID = "KEY_SSAID"
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_REQUIRE_SSAID = "[ERROR] SSAID가 존재하지 않습니다."
        private const val ERROR_REQUIRE_BOTTARI_ID = "[ERROR] 보따리 ID가 존재하지 않습니다."
        private const val DEBOUNCE_DELAY = 250L

        fun Factory(
            ssaid: String,
            bottariId: Long,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_SSAID] = ssaid
                    stateHandle[KEY_BOTTARI_ID] = bottariId
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
