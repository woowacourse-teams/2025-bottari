package com.bottari.data.model.sse

import com.bottari.data.common.util.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

sealed interface SSEDataResponse {
    val publishedAt: LocalDateTime

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
    ) : SSEDataResponse

    @Serializable
    data class SharedItemInfoCreateResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
    ) : SSEDataResponse

    @Serializable
    data class SharedItemInfoDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
    ) : SSEDataResponse

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
    ) : SSEDataResponse

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
    ) : SSEDataResponse

    @Serializable
    data class AssignedItemInfoDeleteResponse(
        @SerialName("publishedAt")
        @Serializable(with = LocalDateTimeSerializer::class)
        override val publishedAt: LocalDateTime,
        @SerialName("infoId")
        val infoId: Long,
        @SerialName("name")
        val name: String,
    ) : SSEDataResponse

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
    ) : SSEDataResponse

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
    ) : SSEDataResponse
}
