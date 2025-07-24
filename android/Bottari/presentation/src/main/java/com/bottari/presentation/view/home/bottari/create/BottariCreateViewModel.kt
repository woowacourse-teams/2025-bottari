package com.bottari.presentation.view.home.bottari.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.CreateBottariUseCase
import com.bottari.presentation.base.UiState
import kotlinx.coroutines.launch

class BottariCreateViewModel(
    private val createBottariUseCase: CreateBottariUseCase,
) : ViewModel() {
    private val _createSuccess = MutableLiveData<UiState<Long?>>()
    val createSuccess: MutableLiveData<UiState<Long?>> = _createSuccess

    fun createBottari(
        ssaid: String,
        title: String,
    ) {
        if (title.isBlank()) return
        _createSuccess.value = UiState.Loading

        viewModelScope.launch {
            createBottariUseCase(ssaid, title)
                .onSuccess { _createSuccess.value = UiState.Success(it) }
                .onFailure { _createSuccess.value = UiState.Failure(it.message) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    BottariCreateViewModel(UseCaseProvider.createBottariUseCase)
                }
            }
    }
}
