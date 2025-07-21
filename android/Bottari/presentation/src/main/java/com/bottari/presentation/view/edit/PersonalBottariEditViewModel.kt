package com.bottari.presentation.view.edit


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.BottariUiModel


class PersonalBottariEditViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _bottari = MutableLiveData<UiState<BottariUiModel>>()
    val bottari: LiveData<UiState<BottariUiModel>> = _bottari

    init {
        val bottariId = savedStateHandle.get<Long>(EXTRAS_BOTTARI_ID)
            ?: error("Bottari ID가 없습니다.")
        fetchBottariById(bottariId)
    }

    private fun fetchBottariById(id: Long) {
        _bottari.value = UiState.Success(dummyBottariUiModel)
    }

    companion object {
        private const val EXTRAS_BOTTARI_ID = "EXTRAS_BOTTARI_ID"

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    val handle = extras.createSavedStateHandle()
                    handle[EXTRAS_BOTTARI_ID] = bottariId
                    return PersonalBottariEditViewModel(handle) as T
                }
            }
    }
}

private val dummyBottariUiModel =
    BottariUiModel(
        id = 1,
        title = "내가 만든 보따리",
        totalQuantity = 0,
        checkedQuantity = 0,
        alarmTypeUiModel = null,
    )

