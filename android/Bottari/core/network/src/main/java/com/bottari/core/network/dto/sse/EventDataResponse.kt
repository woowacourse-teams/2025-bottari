package com.bottari.core.network.dto.sse

import com.bottari.core.domain.model.event.EventData
import com.bottari.data.common.util.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

sealed interface EventDataResponse {
    fun toDomain(): EventData

    @Serializable
    data class TeamMemberCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("isOwner")
        val isOwner: Boolean,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.TeamMemberCreate(publishedAt, teamBottariId, memberId, name, isOwner)
    }

    @Serializable
    data class TeamMemberDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("teamBottariName")
        val teamBottariName: String,
        @SerialName("exitMemberId")
        val exitMemberId: Long,
        @SerialName("exitMemberName")
        val exitMemberName: String,
    ) : EventDataResponse {
        override fun toDomain(): EventData =
            EventData.TeamMemberDelete(
                publishedAt,
                teamBottariId,
                teamBottariName,
                exitMemberId,
                exitMemberName,
            )
    }

    @Serializable
    data class SharedItemInfoCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("infos")
        val infos: List<Info>,
    ) : EventDataResponse {
        @Serializable
        data class Info(
            @SerialName("id")
            val id: Long,
            @SerialName("name")
            val name: String,
        )

        override fun toDomain(): EventData = EventData.SharedItemInfoCreate(publishedAt, teamBottariId, infos.toDomainInfos())

        private fun List<Info>.toDomainInfos(): List<EventData.SharedItemInfoCreate.Info> =
            map { info -> EventData.SharedItemInfoCreate.Info(info.id, info.name) }
    }

    @Serializable
    data class SharedItemCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.SharedItemCreate(publishedAt, teamBottariId)
    }

    @Serializable
    data class SharedItemInfoDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("infos")
        val infos: List<Info>,
    ) : EventDataResponse {
        @Serializable
        data class Info(
            @SerialName("id")
            val id: Long,
            @SerialName("name")
            val name: String,
        )

        override fun toDomain(): EventData = EventData.SharedItemInfoDelete(publishedAt, teamBottariId, infos.toDomainInfos())

        private fun List<Info>.toDomainInfos(): List<EventData.SharedItemInfoDelete.Info> =
            map { info -> EventData.SharedItemInfoDelete.Info(info.id, info.name) }
    }

    @Serializable
    data class SharedItemDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.SharedItemDelete(publishedAt, teamBottariId)
    }

    @Serializable
    data class SharedItemCheckResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("isChecked")
        val isChecked: Boolean,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.SharedItemCheck(publishedAt, infoId, memberId, isChecked, teamBottariId)
    }

    @Serializable
    data class AssignedItemInfoCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("infos")
        val infos: List<Info>,
    ) : EventDataResponse {
        @Serializable
        data class Info(
            @SerialName("id")
            val id: Long,
            @SerialName("name")
            val name: String,
            @SerialName("assignees")
            val assignees: List<Assignee>,
        ) {
            @Serializable
            data class Assignee(
                @SerialName("memberId")
                val memberId: Long,
                @SerialName("name")
                val name: String,
            )
        }

        override fun toDomain(): EventData = EventData.AssignedItemInfoCreate(publishedAt, teamBottariId, infos.toDomainInfos())

        private fun List<Info>.toDomainInfos(): List<EventData.AssignedItemInfoCreate.Info> =
            map { info ->
                EventData.AssignedItemInfoCreate.Info(info.id, info.name, info.assignees.toDomainAssignees())
            }

        private fun List<Info.Assignee>.toDomainAssignees(): List<EventData.AssignedItemInfoCreate.Info.Assignee> =
            map { assignee ->
                EventData.AssignedItemInfoCreate.Info.Assignee(assignee.memberId, assignee.name)
            }
    }

    @Serializable
    data class AssignedItemCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemCreate(publishedAt, teamBottariId)
    }

    @Serializable
    data class AssignedItemInfoChangeResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("memberIds")
        val memberIds: List<Long>,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemInfoChange(publishedAt, infoId, name, memberIds, teamBottariId)
    }

    @Serializable
    data class AssignedItemInfoDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
        @SerialName("infos")
        val infos: List<Info>,
    ) : EventDataResponse {
        @Serializable
        data class Info(
            @SerialName("id")
            val id: Long,
            @SerialName("name")
            val name: String,
            @SerialName("assignees")
            val assignees: List<Assignee>,
        ) {
            @Serializable
            data class Assignee(
                @SerialName("memberId")
                val memberId: Long,
                @SerialName("name")
                val name: String,
            )
        }

        override fun toDomain(): EventData = EventData.AssignedItemInfoDelete(publishedAt, teamBottariId, infos.toDomainInfos())

        private fun List<Info>.toDomainInfos(): List<EventData.AssignedItemInfoDelete.Info> =
            map { info ->
                EventData.AssignedItemInfoDelete.Info(info.id, info.name, info.assignees.toDomainAssignees())
            }

        private fun List<Info.Assignee>.toDomainAssignees(): List<EventData.AssignedItemInfoDelete.Info.Assignee> =
            map { assignee ->
                EventData.AssignedItemInfoDelete.Info.Assignee(assignee.memberId, assignee.name)
            }
    }

    @Serializable
    data class AssignedItemDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemDelete(publishedAt, teamBottariId)
    }

    @Serializable
    data class AssignedItemCheckResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("isChecked")
        val isChecked: Boolean,
        @SerialName("teamBottariId")
        val teamBottariId: Long,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemCheck(publishedAt, infoId, memberId, isChecked, teamBottariId)
    }
}
