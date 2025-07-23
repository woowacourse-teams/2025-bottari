package com.bottari.presentation.view.home.bottari

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.BottariUiModel

class BottariViewModel : ViewModel() {
    private val _bottaries: MutableLiveData<UiState<List<BottariUiModel>>> =
        MutableLiveData(UiState.Loading)
    val bottaries: LiveData<UiState<List<BottariUiModel>>> get() = _bottaries

    init {
        fetchBottaries()
    }

    private fun fetchBottaries() {
        val uiState = UiState.Success(emptyList<BottariUiModel>())
        _bottaries.value = uiState
    }
}
