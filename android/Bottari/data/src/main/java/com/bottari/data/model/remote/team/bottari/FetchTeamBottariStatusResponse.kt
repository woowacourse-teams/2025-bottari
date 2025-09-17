package com.bottari.data.model.remote.team.bottari

import com.bottari.domain.model.bottari.item.BottariItemCount
import com.bottari.domain.model.team.bottari.TeamBottariProductStatus
import com.bottari.domain.model.team.bottari.TeamBottariStatus
import com.bottari.domain.model.team.member.MemberCheckStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchTeamBottariStatusResponse(
    @SerialName("sharedItems")
    val sharedItems: List<FetchTeamBottariStatusItemResponse>,
    @SerialName("assignedItems")
    val assignedItems: List<FetchTeamBottariStatusItemResponse>,
) {
    fun toDomain(): TeamBottariStatus =
        TeamBottariStatus(
            sharedItems = sharedItems.map { it.toDomain() },
            assignedItems = assignedItems.map { it.toDomain() },
        )
}

@Serializable
data class FetchTeamBottariStatusItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("memberCheckStatus")
    val memberCheckStatus: List<TeamMemberStatusCheckedFetchResponse>,
    @SerialName("checkItemsCount")
    val checkItemsCount: Int,
    @SerialName("totalItemsCount")
    val totalItemsCount: Int,
) {
    fun toDomain(): TeamBottariProductStatus =
        TeamBottariProductStatus(
            id = id,
            name = name,
            memberCheckStatus = memberCheckStatus.map { it.toDomain() },
            itemCount =
                BottariItemCount(
                    checkedQuantity = checkItemsCount,
                    totalQuantity = totalItemsCount,
                ),
        )
}

@Serializable
data class TeamMemberStatusCheckedFetchResponse(
    @SerialName("name")
    val name: String,
    @SerialName("checked")
    val checked: Boolean,
) {
    fun toDomain(): MemberCheckStatus =
        MemberCheckStatus(
            name = name,
            checked = checked,
        )
}
