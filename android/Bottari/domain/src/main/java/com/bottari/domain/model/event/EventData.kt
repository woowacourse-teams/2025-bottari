package com.bottari.domain.model.event

import java.time.LocalDateTime

sealed interface EventData {
    val publishedAt: LocalDateTime

    data class TeamMemberCreate(
        override val publishedAt: LocalDateTime,
        val memberId: Long,
        val name: String,
        val isOwner: Boolean,
    ) : EventData

    data class TeamMemberDelete(
        override val publishedAt: LocalDateTime,
        val exitMemberId: Long,
        val bottariId: String,
    ) : EventData

    data class SharedItemInfoCreate(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val name: String,
    ) : EventData

    data class SharedItemInfoDelete(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val name: String,
    ) : EventData

    data class AssignedItemInfoCreate(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val name: String,
        val memberIds: List<Long>,
    ) : EventData

    data class AssignedItemInfoChange(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val name: String,
        val memberIds: List<Long>,
    ) : EventData

    data class AssignedItemInfoDelete(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val name: String,
    ) : EventData

    data class SharedItemChange(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val memberId: Long,
        val isChecked: Boolean,
    ) : EventData

    data class AssignedItemChange(
        override val publishedAt: LocalDateTime,
        val infoId: Long,
        val memberId: Long,
        val isChecked: Boolean,
    ) : EventData
}
