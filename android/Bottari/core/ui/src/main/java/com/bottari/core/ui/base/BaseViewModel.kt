package com.bottari.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bottari.logger.BottariLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState, UiEvent>(
    initialState: UiState,
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    protected val currentState: UiState
        get() = _uiState.value

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> get() = _uiEvent.receiveAsFlow()

    protected val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }

    protected fun updateState(reducer: UiState.() -> UiState) {
        _uiState.update { currentState.reducer() }
    }

    protected fun emitEvent(event: UiEvent) {
        launch { _uiEvent.send(event) }
    }

    protected fun launch(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(exceptionHandler, block = block)

    protected open fun handleError(throwable: Throwable) {
        BottariLogger.error(throwable.localizedMessage, throwable)
    }
}
