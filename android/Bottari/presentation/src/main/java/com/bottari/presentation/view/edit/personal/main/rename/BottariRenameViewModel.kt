package com.bottari.presentation.view.edit.personal.main.rename

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.usecase.bottari.RenameBottariUseCase
import com.bottari.presentation.base.UiState
import kotlinx.coroutines.launch

class BottariRenameViewModel(
    private val renameBottariUseCase: RenameBottariUseCase,
) : ViewModel() {
    private val _renameSuccess = MutableLiveData<UiState<Unit?>>()
    val renameSuccess: MutableLiveData<UiState<Unit?>> = _renameSuccess

    fun renameBottari(
        id: Long,
        oldTitle: String,
        ssaid: String,
        title: String,
    ) {
        if (title.isBlank()){
            _renameSuccess.value = UiState.Failure("이름을 빈칸으로 지정할 수 없습니다")
            return
        }
        if (title == oldTitle) {
            _renameSuccess.value = UiState.Failure("기존과 똑같은 이름으로 변경할 수 없습니다")
            return
        }
        _renameSuccess.value = UiState.Loading

        viewModelScope.launch {
            renameBottariUseCase(id, ssaid, title)
                .onSuccess { _renameSuccess.value = UiState.Success(it) }
                .onFailure { _renameSuccess.value = UiState.Failure(it.message) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    BottariRenameViewModel(UseCaseProvider.renameBottariUseCase)
                }
            }
    }
}
