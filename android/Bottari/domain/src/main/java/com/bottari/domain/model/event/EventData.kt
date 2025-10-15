package com.bottari.domain.model.event

import java.time.LocalDateTime

sealed interface EventData {
    data class TeamMemberCreate(
        val publishedAt: LocalDateTime,
        val memberId: Long,
        val name: String,
        val isOwner: Boolean,
    ) : EventData

    data class TeamMemberDelete(
        val publishedAt: LocalDateTime,
        val bottariId: String,
        val bottariName: String,
        val exitMemberId: Long,
        val exitMemberName: String,
    ) : EventData

    data class SharedItemInfoCreate(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
        val infos: List<Info>,
    ) : EventData {
        data class Info(
            val id: Long,
            val name: String,
        )
    }

    data class SharedItemCreate(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
    ) : EventData

    data class SharedItemInfoDelete(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
        val infos: List<Info>,
    ) : EventData {
        data class Info(
            val id: Long,
            val name: String,
        )
    }

    data class SharedItemDelete(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
    ) : EventData

    data class SharedItemCheck(
        val publishedAt: LocalDateTime,
        val infoId: Long,
        val memberId: Long,
        val isChecked: Boolean,
    ) : EventData

    data class AssignedItemInfoCreate(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
        val infos: List<Info>,
    ) : EventData {
        data class Info(
            val id: Long,
            val name: String,
            val assignees: List<Assignee>,
        ) {
            data class Assignee(
                val memberId: Long,
                val name: String,
            )
        }
    }

    data class AssignedItemCreate(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
    ) : EventData

    data class AssignedItemInfoChange(
        val publishedAt: LocalDateTime,
        val infoId: Long,
        val name: String,
        val memberIds: List<Long>,
    ) : EventData

    data class AssignedItemInfoDelete(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
        val infos: List<Info>,
    ) : EventData {
        data class Info(
            val id: Long,
            val name: String,
            val assignees: List<Assignee>,
        ) {
            data class Assignee(
                val memberId: Long,
                val name: String,
            )
        }
    }

    data class AssignedItemDelete(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
    ) : EventData

    data class AssignedItemCheck(
        val publishedAt: LocalDateTime,
        val infoId: Long,
        val memberId: Long,
        val isChecked: Boolean,
    ) : EventData
}
