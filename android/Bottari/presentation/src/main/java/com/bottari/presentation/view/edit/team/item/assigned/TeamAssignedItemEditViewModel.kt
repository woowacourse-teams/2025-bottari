package com.bottari.presentation.view.edit.team.item.assigned

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.team.TeamMember
import com.bottari.domain.usecase.team.CreateTeamAssignedItemUseCase
import com.bottari.domain.usecase.team.DeleteTeamBottariItemUseCase
import com.bottari.domain.usecase.team.FetchTeamAssignedItemsUseCase
import com.bottari.domain.usecase.team.FetchTeamBottariMembersUseCase
import com.bottari.presentation.common.base.BaseViewModel
import com.bottari.presentation.mapper.TeamBottariMapper.toUiModel
import com.bottari.presentation.mapper.TeamMembersMapper.toUiModel
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.model.BottariItemUiModel
import com.bottari.presentation.model.TeamMemberUiModel
import kotlinx.coroutines.async

class TeamAssignedItemEditViewModel(
    stateHandle: SavedStateHandle,
    private val fetchTeamAssignedItemsUseCase: FetchTeamAssignedItemsUseCase,
    private val createTeamAssignedItemUseCase: CreateTeamAssignedItemUseCase,
    private val deleteTeamBottariItemUseCase: DeleteTeamBottariItemUseCase,
    private val fetchTeamBottariMembersUseCase: FetchTeamBottariMembersUseCase,
) : BaseViewModel<TeamAssignedItemEditUiState, TeamAssignedItemEditEvent>(
        TeamAssignedItemEditUiState(),
    ) {
    private val bottariId: Long = stateHandle[KEY_BOTTARI_ID] ?: error(ERROR_BOTTARI_ID)

    init {
        refreshAssignedItemsAndMembers()
    }

    fun updateInput(input: String) {
        if (currentState.inputText == input) return
        updateState { copy(inputText = input) }
        currentState.matchingItem.let(::applyAssignedMembersSelection)
    }

    fun selectAssignedItem(itemId: Long) {
        val selectedItem = currentState.assignedItems.find { it.id == itemId } ?: return
        applyAssignedMembersSelection(selectedItem)
    }

    fun selectMember(memberId: Long) = updateState { copy(members = toggleMemberSelection(memberId)) }

    fun createItem() {
        updateState { copy(isLoading = true) }

        launch {
            createTeamAssignedItemUseCase(
                bottariId,
                currentState.inputText,
                currentState.selectedMemberIds,
            ).onSuccess {
                refreshAssignedItemsAndMembers()
                emitEvent(TeamAssignedItemEditEvent.CreateItemSuccess)
            }.onFailure { emitEvent(TeamAssignedItemEditEvent.CreateItemFailure) }

            updateState { copy(isLoading = false) }
        }
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
                    isLoading = false,
                    isFetched = true,
                )
            }
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

    private fun applyAssignedMembersSelection(selectedItem: BottariItemUiModel?) {
        if (selectedItem == null) {
            clearMemberSelections()
            return
        }

        val assignedMembersIds = selectedItem.assignedMemberIds()
        val updatedMembers = mapMembersWithAssignedIds(assignedMembersIds)
        updateState { copy(members = updatedMembers) }
    }

    private fun clearMemberSelections() {
        val clearedMembers = currentState.members.map { it.copy(isHost = false) }
        updateState { copy(members = clearedMembers) }
    }

    private fun BottariItemUiModel.assignedMemberIds(): List<Long> =
        (type as? BottariItemTypeUiModel.ASSIGNED)
            ?.members
            ?.mapNotNull { it.id }
            ?: emptyList()

    private fun mapMembersWithAssignedIds(assignedMembersIds: List<Long>): List<TeamMemberUiModel> =
        currentState.members.map { member ->
            member.copy(isHost = member.id in assignedMembersIds)
        }

    companion object {
        private const val KEY_BOTTARI_ID = "KEY_BOTTARI_ID"
        private const val ERROR_BOTTARI_ID = "[ERROR] bottariId가 존재하지 않습니다"

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
                    )
                }
            }
    }
}
