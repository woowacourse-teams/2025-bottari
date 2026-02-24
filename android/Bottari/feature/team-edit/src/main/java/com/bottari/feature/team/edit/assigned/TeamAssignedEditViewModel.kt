package com.bottari.feature.team.edit.assigned

import com.bottari.core.domain.model.bottari.item.BottariItem
import com.bottari.core.domain.model.event.EventData
import com.bottari.core.domain.model.event.EventState
import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.core.domain.model.team.member.TeamMember
import com.bottari.core.domain.usecase.event.ConnectTeamEventUseCase
import com.bottari.core.domain.usecase.team.CreateTeamAssignedItemUseCase
import com.bottari.core.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.core.domain.usecase.team.FetchTeamAssignedItemsUseCase
import com.bottari.core.domain.usecase.team.FetchTeamBottariMembersUseCase
import com.bottari.core.domain.usecase.team.SaveTeamBottariAssignedItemUseCase
import com.bottari.core.ui.base.BaseViewModel
import com.bottari.core.ui.model.bottari.personal.BottariItemTypeUiModel
import com.bottari.core.ui.model.bottari.personal.SelectableItemUiModel
import com.bottari.core.ui.model.bottari.team.member.TeamMemberUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map

@HiltViewModel(assistedFactory = TeamAssignedEditViewModel.Factory::class)
class TeamAssignedEditViewModel @AssistedInject constructor(
    @Assisted private val bottariId: Long,
    private val fetchTeamAssignedItemsUseCase: FetchTeamAssignedItemsUseCase,
    private val createTeamAssignedItemUseCase: CreateTeamAssignedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val fetchTeamBottariMembersUseCase: FetchTeamBottariMembersUseCase,
    private val saveTeamBottariAssignedItemUseCase: SaveTeamBottariAssignedItemUseCase,
    private val connectTeamEventUseCase: ConnectTeamEventUseCase,
) : BaseViewModel<TeamAssignedEditUiState, TeamAssignedEditUiEvent>(
        TeamAssignedEditUiState(),
    ) {
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

        applyAssignedMembersSelection()
        applyTextInput(itemId)
    }

    fun resetState() =
        updateState {
            copy(
                isCreating = false,
                inputText = "",
                members = members.map { member -> member.copy(isHost = false) },
                assignedItems = assignedItems.map { item -> item.copy(isSelected = false) },
            )
        }

    fun selectMember(memberId: Long) = updateState { copy(members = toggleMemberSelection(memberId)) }

    fun selectAllMember() = updateState { copy(members = currentState.members.map { it.copy(isHost = true) }) }

    fun unSelectAllMember() = updateState { copy(members = currentState.members.map { it.copy(isHost = false) }) }

    fun submitItem() {
        if (currentState.isEditing) saveAssignedItem() else createAssignedItem()
        resetState()
    }

    fun deleteItem(itemId: Long) {
        updateState { copy(isLoading = true) }

        launch {
            deleteTeamBottariItemUseCase(itemId, TeamBottariItemType.ASSIGNED())
                .onSuccess { refreshAssignedItemsAndMembers() }
                .onFailure { emitEvent(TeamAssignedEditUiEvent.DeleteItemFailure) }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    fun refreshAssignedItemsAndMembers() {
        updateState { copy(isLoading = true) }

        launch {
            val assignedItemsDeferred = async { loadAssignedItems() }
            val membersDeferred = async { loadTeamMembers() }

            val assignedItems = assignedItemsDeferred.await()
            val members = membersDeferred.await()

            updateState {
                copy(
                    assignedItems = syncAssignedItems(assignedItems.map(SelectableItemUiModel::fromDomain)),
                    members = syncMembers(members.map(TeamMemberUiModel::fromDomain)),
                    isFetched = true,
                    isLoading = false,
                )
            }
        }
    }

    private fun applyTextInput(itemId: Long) {
        val item = currentState.assignedItems.find { it.id == itemId }
        updateState { copy(inputText = item?.name ?: "") }
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
            }.onFailure {
                emitEvent(TeamAssignedEditUiEvent.CreateItemFailure)
            }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    fun openBottomSheetState() {
        updateState { copy(isCreating = true) }
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
            }.onFailure {
                emitEvent(TeamAssignedEditUiEvent.SaveItemFailure)
            }
        }.invokeOnCompletion { updateState { copy(isLoading = false) } }
    }

    private fun syncAssignedItems(items: List<SelectableItemUiModel>): List<SelectableItemUiModel> {
        val currentState =
            items.map { item ->
                val found = currentState.selectedAssignedItem ?: return@map item
                if (found.id == item.id) item.copy(isSelected = true) else item
            }

        return currentState
    }

    private fun syncMembers(members: List<TeamMemberUiModel>): List<TeamMemberUiModel> {
        val currentState =
            members.map { member ->
                val found = currentState.members.find { it.id == member.id } ?: return@map member
                member.copy(isHost = found.isHost)
            }
        return currentState
    }

    @OptIn(FlowPreview::class)
    private fun handleEvent() {
        launch {
            connectTeamEventUseCase()
                .filterIsInstance<EventState.OnEvent>()
                .map { event -> event.data }
                .filterNot { eventData -> eventData.shouldIgnore() }
                .debounce(DEBOUNCE_DELAY)
                .collect { refreshAssignedItemsAndMembers() }
        }
    }

    private suspend fun loadAssignedItems(): List<BottariItem> =
        fetchTeamAssignedItemsUseCase(bottariId)
            .onFailure { emitEvent(TeamAssignedEditUiEvent.FetchTeamAssignedItemsFailure) }
            .getOrElse { emptyList() }

    private suspend fun loadTeamMembers(): List<TeamMember> =
        fetchTeamBottariMembersUseCase(bottariId)
            .onFailure { emitEvent(TeamAssignedEditUiEvent.FetchTeamAssignedItemsFailure) }
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

    private fun SelectableItemUiModel?.assignedMemberIds(): List<Long> =
        (this?.type as? BottariItemTypeUiModel.ASSIGNED)
            ?.members
            ?.mapNotNull { it.id }
            ?: emptyList()

    private fun SelectableItemUiModel.toggleSelection(targetId: Long): SelectableItemUiModel =
        copy(isSelected = (id == targetId && !isSelected))

    private fun mapMembersWithAssignedIds(ids: List<Long>): List<TeamMemberUiModel> =
        currentState.members.map { it.copy(isHost = it.id in ids) }

    private fun EventData.shouldIgnore(): Boolean =
        when (this) {
            is EventData.AssignedItemInfoCreate,
            is EventData.AssignedItemInfoDelete,
            is EventData.AssignedItemInfoChange,
            is EventData.TeamMemberCreate,
            is EventData.TeamMemberDelete,
            -> false

            else -> true
        }

    @AssistedFactory
    interface Factory {
        fun create(bottariId: Long): TeamAssignedEditViewModel
    }

    companion object {
        private const val DEBOUNCE_DELAY = 300L
    }
}
