package com.bottari.data.mapper

import com.bottari.data.model.item.FetchChecklistResponse
import com.bottari.domain.model.bottari.BottariItem

object BottariItemMapper {
    fun FetchChecklistResponse.toDomain(): BottariItem = BottariItem(
        id = id,
        name = name,
        isChecked = isChecked,
    )
}
