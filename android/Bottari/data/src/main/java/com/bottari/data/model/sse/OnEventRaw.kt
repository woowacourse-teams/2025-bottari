package com.bottari.data.model.sse

import com.bottari.data.common.util.LocalDateTimeSerializer
import com.bottari.logger.BottariLogger
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.time.LocalDateTime

@Serializable
data class OnEventRaw(
    @SerialName("resource")
    val resource: ResourceResponse,
    @SerialName("event")
    val event: EventResponse,
    @SerialName("data")
    val data: JsonElement,
    @SerialName("publishedAt")
    @Serializable(with = LocalDateTimeSerializer::class)
    val publishedAt: LocalDateTime,
)

fun OnEventRaw.toEvent(json: Json): SSEEventState.OnEvent {
    BottariLogger.debug("resource: $resource, event: $event")
    val eventData: SSEDataResponse =
        when (resource to event) {
            ResourceResponse.TEAM_MEMBER to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.TeamMemberCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM to EventResponse.CHANGE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.SharedItemChangeResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM_INFO to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.SharedItemInfoCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM_INFO to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.SharedItemInfoDeleteResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM to EventResponse.CHANGE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.AssignedItemChangeResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM_INFO to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.AssignedItemInfoCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM_INFO to EventResponse.CHANGE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.AssignedItemInfoChangeResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM_INFO to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    SSEDataResponse.AssignedItemInfoDeleteResponse.serializer(),
                    data,
                )

            else -> throw IllegalArgumentException("[ERROR] Unknown event or resource.")
        }
    return SSEEventState.OnEvent(resource, event, eventData, publishedAt)
}
