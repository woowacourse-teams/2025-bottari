package com.bottari.core.network.dto.sse

import com.bottari.data.common.util.LocalDateTimeSerializer
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

fun OnEventRaw.toEvent(json: Json): EventStateResponse.OnEventResponse {
    val eventData: EventDataResponse =
        when (resource to event) {
            ResourceResponse.TEAM_MEMBER to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    EventDataResponse.TeamMemberCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.TEAM_MEMBER to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    EventDataResponse.TeamMemberDeleteResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM_INFO to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    EventDataResponse.SharedItemInfoCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM_INFO to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    EventDataResponse.SharedItemInfoDeleteResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    EventDataResponse.SharedItemCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    EventDataResponse.SharedItemDeleteResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM to EventResponse.CHECK ->
                json.decodeFromJsonElement(
                    EventDataResponse.SharedItemCheckResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM_INFO to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemInfoCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM_INFO to EventResponse.CHANGE ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemInfoChangeResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM_INFO to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemInfoDeleteResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM to EventResponse.DELETE ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemDeleteResponse.serializer(),
                    data,
                )

            ResourceResponse.ASSIGNED_ITEM to EventResponse.CHECK ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemCheckResponse.serializer(),
                    data,
                )

            else -> throw IllegalArgumentException("[ERROR] Unknown event or resource.")
        }
    return EventStateResponse.OnEventResponse(resource, event, eventData, publishedAt)
}
