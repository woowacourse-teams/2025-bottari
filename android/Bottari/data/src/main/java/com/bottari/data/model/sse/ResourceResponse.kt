package com.bottari.data.model.sse

import kotlinx.serialization.Serializable

@Serializable
enum class ResourceResponse {
    TEAM_MEMBER,
    SHARED_ITEM,
    SHARED_ITEM_INFO,
    ASSIGNED_ITEM,
    ASSIGNED_ITEM_INFO,
}
