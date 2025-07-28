package com.bottari.presentation.view.market.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.BottariTemplateUiModel
import kotlinx.coroutines.launch

class MyBottariTemplateViewModel : ViewModel() {
    private val _myBottariTemplates: MutableLiveData<UiState<List<BottariTemplateUiModel>>> =
        MutableLiveData(UiState.Loading)
    val myBottariTemplates: LiveData<UiState<List<BottariTemplateUiModel>>> get() = _myBottariTemplates

    init {
        fetchMyBottariTemplates()
    }

    fun deleteBottariTemplate(bottariTemplateId: Long) {
        viewModelScope.launch { }
    }

    private fun fetchMyBottariTemplates() {
        viewModelScope.launch {
            _myBottariTemplates.value = UiState.Success(listOf())
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MyBottariTemplateViewModel()
                }
            }
    }
}
