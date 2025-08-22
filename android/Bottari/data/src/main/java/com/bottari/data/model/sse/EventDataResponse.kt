package com.bottari.data.model.sse

import com.bottari.data.common.util.LocalDateTimeSerializer
import com.bottari.domain.model.event.EventData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

sealed interface EventDataResponse {
    val publishedAt: LocalDateTime

    fun toDomain(): EventData

    @Serializable
    data class TeamMemberCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("isOwner")
        val isOwner: Boolean,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.TeamMemberCreate(publishedAt, memberId, name, isOwner)
    }

    @Serializable
    data class TeamMemberDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("exitMemberId")
        val exitMemberId: Long,
        @SerialName("bottariId")
        val bottariId: String,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.TeamMemberDelete(publishedAt, exitMemberId, bottariId)
    }

    @Serializable
    data class SharedItemInfoCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.SharedItemInfoCreate(publishedAt, infoId, name)
    }

    @Serializable
    data class SharedItemInfoDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.SharedItemInfoDelete(publishedAt, infoId, name)
    }

    @Serializable
    data class AssignedItemInfoCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("memberIds")
        val memberIds: List<Long>,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemInfoCreate(publishedAt, infoId, name, memberIds)
    }

    @Serializable
    data class AssignedItemInfoChangeResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
        @SerialName("memberIds")
        val memberIds: List<Long>,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemInfoChange(publishedAt, infoId, name, memberIds)
    }

    @Serializable
    data class AssignedItemInfoDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemInfoDelete(publishedAt, infoId, name)
    }

    @Serializable
    data class SharedItemChangeResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("isChecked")
        val isChecked: Boolean,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.SharedItemChange(publishedAt, infoId, memberId, isChecked)
    }

    @Serializable
    data class AssignedItemChangeResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("memberId")
        val memberId: Long,
        @SerialName("isChecked")
        val isChecked: Boolean,
    ) : EventDataResponse {
        override fun toDomain(): EventData = EventData.AssignedItemChange(publishedAt, infoId, memberId, isChecked)
    }
}
