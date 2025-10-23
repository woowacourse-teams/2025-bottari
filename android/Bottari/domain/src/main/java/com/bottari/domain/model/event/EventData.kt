package com.bottari.domain.model.event

import java.time.LocalDateTime

/*
 * 현재 물건 관련 이벤트가 Item과 ItemInfo로 나누어져 있습니다.
 * 하지만 실제로는 두 이벤트가 항상 동시에 발생하기 때문에 중복 처리가 생기는 문제가 있습니다.
 * 따라서 View에서는 payload가 포함된 ItemInfo만 사용하도록 했습니다.
 */

sealed interface EventData {
    data class TeamMemberCreate(
        val publishedAt: LocalDateTime,
        val teamBottariId: Long,
        val memberId: Long,
        val name: String,
        val isOwner: Boolean,
    ) : EventData

    data class TeamMemberDelete(
        val publishedAt: LocalDateTime,
        val teamBottariId: String,
        val teamBottariName: String,
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
        val teamBottariId: Long,
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

        fun containMember(memberId: Long): Boolean =
            infos.any { info ->
                info.assignees.any { assignee -> assignee.memberId == memberId }
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
        val teamBottariId: Long,
    ) : EventData {
        fun containMember(memberId: Long): Boolean = memberIds.contains(memberId)
    }

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

        fun containMember(memberId: Long): Boolean =
            infos.any { info ->
                info.assignees.any { assignee -> assignee.memberId == memberId }
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
        val teamBottariId: Long,
    ) : EventData
}
