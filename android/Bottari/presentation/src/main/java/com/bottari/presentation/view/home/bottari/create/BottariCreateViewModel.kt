package com.bottari.presentation.view.home.bottari.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bottari.presentation.base.UiState

class BottariCreateViewModel : ViewModel() {
    private val _createSuccess = MutableLiveData<UiState<Long>>()
    val createSuccess: MutableLiveData<UiState<Long>> = _createSuccess

    fun createBottari(name: String) {
        _createSuccess.value = UiState.Success(1L)
    }
}
