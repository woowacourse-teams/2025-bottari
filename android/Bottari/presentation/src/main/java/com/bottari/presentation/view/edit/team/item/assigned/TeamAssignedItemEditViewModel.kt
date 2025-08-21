package com.bottari.presentation.view.edit.team.item.assigned

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.event.EventData
import com.bottari.domain.model.event.EventState
import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.domain.usecase.team.CreateTeamAssignedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamAssignedItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariMembersUseCase
import com.bottari.domain.usecase.team.SaveTeamBottariAssignedItemUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamMemberUiModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TeamAssignedItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamAssignedItemsUseCase: FetchTeamAssignedItemsUseCase,
    private val createTeamAssignedItemUseCase: CreateTeamAssignedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val fetchTeamBottariMembersUseCase: FetchTeamBottariMembersUseCase,
    private val saveTeamBottariAssignedItemUseCase: SaveTeamBottariAssignedItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
) : BaseViewModel<TeamAssignedItemEditUiState, TeamAssignedItemEditEvent>(
        TeamAssignedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        refreshAssignedItemsAndMembers()
        handleEvent()
    }

    fun updateInput(input: String) {
        if (currentState.inputText == input) return
        updateState { copy(inputText = input) }
    }

    fun toggleEditState(itemId: Long) {
        val newItems =
            currentState.assignedItems.map { assignedItem -> assignedItem.toggleSelection(itemId) }
        updateState { copy(assignedItems = newItems, hasRestoreState = false) }

        currentState.selectedAssignedItem?.name?.let { itemName ->
            emitEvent(TeamAssignedItemEditEvent.SelectAssignedItem(itemName))
        }
        applyAssignedMembersSelection()
    }

    fun selectMember(memberId: Long) = updateState { copy(members = toggleMemberSelection(memberId)) }

    fun submitItem() {
        if (currentState.isEditing) return saveAssignedItem()
        createAssignedItem()
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, BottariItemType.ASSIGNED())
                .onSuccess { refreshAssignedItemsAndMembers() }
                .onFailure { emitEvent(TeamAssignedItemEditEvent.DeleteItemFailure) }

            updateState { copy(isLoading = false) }
        }
    }

    fun saveSelectedState() {
        if (currentState.hasRestoreState) return
        updateState { copy(hasRestoreState = true) }
    }

    private fun createAssignedItem() {
        updateState { copy(isLoading = true) }

        launch {
            createTeamAssignedItemUseCase(
                bottariId,
                currentState.inputText,
                currentState.selectedMemberIds,
            ).onSuccess {
                refreshAssignedItemsAndMembers()
                emitEvent(TeamAssignedItemEditEvent.CreateItemSuccess)
            }.onFailure {
                emitEvent(TeamAssignedItemEditEvent.CreateItemFailure)
            }

            updateState { copy(isLoading = false) }
        }
    }

    private fun saveAssignedItem() {
        updateState { copy(isLoading = true) }

        launch {
            saveTeamBottariAssignedItemUseCase(
                bottariId,
                requireNotNull(currentState.selectedAssignedItem?.id),
                currentState.inputText,
                currentState.selectedMemberIds,
            ).onSuccess {
                refreshAssignedItemsAndMembers()
                emitEvent(TeamAssignedItemEditEvent.SaveItemSuccess)
            }.onFailure {
                emitEvent(TeamAssignedItemEditEvent.SaveItemFailure)
            }

            updateState { copy(isLoading = false) }
        }
    }

    private fun refreshAssignedItemsAndMembers() {
        updateState { copy(isLoading = true) }

        launch {
            val assignedItemsDeferred = async { loadAssignedItems() }
            val membersDeferred = async { loadTeamMembers() }

            val assignedItems = assignedItemsDeferred.await()
            val members = membersDeferred.await()

            updateState {
                copy(
                    assignedItems = assignedItems.map { it.toUiModel() },
                    members = members.map { it.toUiModel() },
                    isFetched = true,
                )
            }

            updateState { copy(isLoading = false, isFetched = true) }
        }
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase(bottariId)
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filterNot { eventData -> eventData.shouldIgnore() }
                .debounce(DEBOUNCE_DELAY)
                .onEach { refreshAssignedItemsAndMembers() }
                .launchIn(this)
        }
    }

    private suspend fun loadAssignedItems(): List<BottariItem> =
        fetchTeamAssignedItemsUseCase(bottariId)
            .onFailure { emitEvent(TeamAssignedItemEditEvent.FetchTeamAssignedItemsFailure) }
            .getOrElse { emptyList() }

    private suspend fun loadTeamMembers(): List<TeamMember> =
        fetchTeamBottariMembersUseCase(bottariId)
            .onFailure { emitEvent(TeamAssignedItemEditEvent.FetchTeamAssignedItemsFailure) }
            .getOrElse { emptyList() }

    private fun toggleMemberSelection(memberId: Long): List<TeamMemberUiModel> =
        currentState.members.map { member ->
            if (member.id != memberId) member else member.copy(isHost = !member.isHost)
        }

    private fun applyAssignedMembersSelection() {
        if (!currentState.isEditing) {
            clearMemberSelections()
            return
        }
        val assignedIds = currentState.selectedAssignedItem.assignedMemberIds()
        val updatedMembers = mapMembersWithAssignedIds(assignedIds)
        updateState { copy(members = updatedMembers) }
    }

    private fun clearMemberSelections() {
        val cleared = currentState.members.map { it.copy(isHost = false) }
        updateState { copy(members = cleared) }
    }

    private fun BottariItemUiModel?.assignedMemberIds(): List<Long> =
        (this?.type as? BottariItemTypeUiModel.ASSIGNED)
            ?.members
            ?.mapNotNull { it.id }
            ?: emptyList()

    private fun BottariItemUiModel.toggleSelection(targetId: Long): BottariItemUiModel = copy(isSelected = (id == targetId && !isSelected))

    private fun mapMembersWithAssignedIds(ids: List<Long>): List<TeamMemberUiModel> =
        currentState.members.map { it.copy(isHost = it.id in ids) }

    private fun EventData.shouldIgnore(): Boolean =
        when (this) {
            is EventData.SharedItemChange,
            is EventData.SharedItemInfoCreate,
            is EventData.SharedItemInfoDelete,
            -> true

            else -> false
        }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] bottariId가 존재하지 않습니다"
        private const val DEBOUNCE_DELAY = 300L

        fun Factory(bottariId: Long): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val stateHandle = createSavedStateHandle()
                    stateHandle[KEY_BOTTARI_ID] = bottariId
                    TeamAssignedItemEditViewModel(
                        stateHandle,
                        UseCaseProvider.fetchTeamAssignedItemsUseCase,
                        UseCaseProvider.createTeamAssignedItemUseCase,
                        UseCaseProvider.deleteTeamBottariItemUseCase,
                        UseCaseProvider.fetchTeamBottariMembersUseCase,
                        UseCaseProvider.saveTeamBottariAssignedItemUseCase,
                        UseCaseProvider.connectTeamEventUseCase,
                    )
                }
            }
    }
}
