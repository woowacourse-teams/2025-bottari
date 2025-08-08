package com.bottari.presentation.common.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottari.logger.BottariLogger
import com.bottari.presentation.common.event.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState, UiEvent>(
    initialState: UiState,
) : ViewModel() {
    private val _uiState = MutableLiveData(initialState)
    val uiState: LiveData<UiState> get() = _uiState

    protected val currentState: UiState
        get() = _uiState.value ?: error(ERROR_STATE_IS_NOT_INITIALIZED)

    private val _uiEvent = SingleLiveEvent<UiEvent>()
    val uiEvent: LiveData<UiEvent> get() = _uiEvent

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }

    protected fun updateState(reducer: UiState.() -> UiState) {
        _uiState.value = currentState.reducer()
    }

    protected fun emitEvent(event: UiEvent) {
        _uiEvent.value = event
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler, block = block)
    }

    protected open fun handleError(throwable: Throwable) {
        BottariLogger.error(throwable.localizedMessage, throwable)
    }

    companion object {
        private const val ERROR_STATE_IS_NOT_INITIALIZED = "[ERROR] UiState가 초기화되지 않았습니다"
    }
}
