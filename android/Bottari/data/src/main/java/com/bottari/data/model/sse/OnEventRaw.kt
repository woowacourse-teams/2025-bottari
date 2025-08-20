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

fun OnEventRaw.toEvent(json: Json): EventStateResponse.OnEventResponse {
    BottariLogger.debug("resource: $resource, event: $event")
    val eventData: EventDataResponse =
        when (resource to event) {
            ResourceResponse.TEAM_MEMBER to EventResponse.CREATE ->
                json.decodeFromJsonElement(
                    EventDataResponse.TeamMemberCreateResponse.serializer(),
                    data,
                )

            ResourceResponse.SHARED_ITEM to EventResponse.CHANGE ->
                json.decodeFromJsonElement(
                    EventDataResponse.SharedItemChangeResponse.serializer(),
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

            ResourceResponse.ASSIGNED_ITEM to EventResponse.CHANGE ->
                json.decodeFromJsonElement(
                    EventDataResponse.AssignedItemChangeResponse.serializer(),
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

            else -> throw IllegalArgumentException("[ERROR] Unknown event or resource.")
        }
    return EventStateResponse.OnEventResponse(resource, event, eventData, publishedAt)
}
