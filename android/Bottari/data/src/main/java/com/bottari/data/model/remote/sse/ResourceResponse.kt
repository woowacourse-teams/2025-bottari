package com.bottari.data.model.remote.sse

import kotlinx.serialization.Serializable

@Serializable
enum class ResourceResponse {
    TEAM_MEMBER,
    SHARED_ITEM_INFO,
    SHARED_ITEM,
    ASSIGNED_ITEM_INFO,
    ASSIGNED_ITEM,
}
