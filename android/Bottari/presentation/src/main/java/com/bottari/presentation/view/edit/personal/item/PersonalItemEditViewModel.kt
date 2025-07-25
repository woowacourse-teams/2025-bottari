package com.bottari.presentation.view.edit.personal.item

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
import com.bottari.domain.usecase.item.SaveBottariItemsUseCase
import com.bottari.presentation.base.UiState
import com.bottari.presentation.model.BottariDetailUiModel
import com.bottari.presentation.model.BottariItemUiModel
import kotlinx.coroutines.launch

class PersonalItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val saveBottariItemsUseCase: SaveBottariItemsUseCase,
) : ViewModel() {
    private val ssaid = stateHandle.getOrThrow<String>(EXTRA_BOTTARI_ID)
    private val bottariDetail = stateHandle.getOrThrow<BottariDetailUiModel>(EXTRA_BOTTARI_DETAIL)

    private val bottariId = bottariDetail.id
    private val initialItemIds = bottariDetail.items.map { it.id }.toSet()

    private val newItemNames = mutableSetOf<String>()
    private val pendingDeleteItems = mutableSetOf<BottariItemUiModel>()

    private val _saveState = MutableLiveData<UiState<Unit>>()
    val saveState: LiveData<UiState<Unit>> = _saveState

    private val _bottariName = MutableLiveData(bottariDetail.title)
    val bottariName: LiveData<String> = _bottariName

    private val _bottariItems = MutableLiveData(bottariDetail.items)
    val bottariItems: LiveData<List<BottariItemUiModel>> = _bottariItems

    private val currentItemList: List<BottariItemUiModel>
        get() = _bottariItems.value.orEmpty()

    fun addNewItemIfNeeded(itemName: String) {
        if (itemName.isBlank() || isDuplicateItem(itemName)) return

        pendingDeleteItems.firstOrNull { it.name == itemName }?.let {
            pendingDeleteItems.remove(it)
            _bottariItems.value = currentItemList + it
            return
        }

        _bottariItems.value = currentItemList + generateNewItemUiModel(itemName)
        newItemNames.add(itemName)
    }

    fun markItemAsDeleted(itemId: Long) {
        val target = currentItemList.find { it.id == itemId } ?: return
        _bottariItems.value = currentItemList.filterNot { it.id == itemId }

        if (initialItemIds.contains(itemId)) {
            pendingDeleteItems.add(target)
            return
        }

        newItemNames.remove(target.name)
        pendingDeleteItems.remove(target)
    }

    fun saveChanges() {
        _saveState.value = UiState.Loading

        viewModelScope.launch {
            val deletedIds = pendingDeleteItems.map { it.id }

            saveBottariItemsUseCase(
                ssaid = ssaid,
                bottariId = bottariId,
                deleteItemIds = deletedIds,
                createItemNames = newItemNames.toList(),
            ).onSuccess {
                _saveState.value = UiState.Success(Unit)
            }.onFailure {
                _saveState.value = UiState.Failure(it.message)
            }
        }
    }

    private fun isDuplicateItem(name: String): Boolean = currentItemList.any { it.name == name }

    private fun generateNewItemUiModel(name: String): BottariItemUiModel =
        BottariItemUiModel(
            id = nextGeneratedItemId(),
            isChecked = false,
            name = name,
        )

    private fun nextGeneratedItemId(): Long = (currentItemList.maxOfOrNull { it.id } ?: DEFAULT_ITEM_ID) + ITEM_ID_INCREMENT

    companion object {
        private const val EXTRA_BOTTARI_ID = "EXTRA_BOTTARI_ID"
        private const val EXTRA_BOTTARI_DETAIL = "EXTRA_BOTTARI_DETAIL"
        private const val ERROR_REQUIRE_SSAID = "SSAID 없습니다"
        private const val ERROR_REQUIRE_BOTTARI_DATA = "보따리가 없습니다"

        private const val DEFAULT_ITEM_ID = 0L
        private const val ITEM_ID_INCREMENT = 1

        fun Factory(
            ssaid: String,
            bottariDetail: BottariDetailUiModel,
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle =
                        createSavedStateHandle().apply {
                            this[EXTRA_BOTTARI_ID] = ssaid
                            this[EXTRA_BOTTARI_DETAIL] = bottariDetail
                        }

                    PersonalItemEditViewModel(
                        stateHandle = stateHandle,
                        saveBottariItemsUseCase = UseCaseProvider.saveBottariItemsUseCase,
                    )
                }
            }

        private inline fun <reified T> SavedStateHandle.getOrThrow(key: String): T =
            requireNotNull(this[key]) {
                if (key == EXTRA_BOTTARI_ID) {
                    ERROR_REQUIRE_SSAID
                } else {
                    ERROR_REQUIRE_BOTTARI_DATA
                }
            }
    }
}
