package com.bottari.data.model.remote.bottari.item

import com.bottari.domain.model.bottari.item.ChecklistItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemFetchResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("isChecked")
    val isChecked: Boolean,
) {
    fun toDomain(): ChecklistItem =
        ChecklistItem(
            id = id,
            name = name,
            isChecked = isChecked,
        )
}
