package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.ItemResponse
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem

object BottariResponseMapper {
    fun BottariResponse.toDomain(): BottariDetail =
        BottariDetail(
            id = id.toLong(),
            title = title,
            alarm = alarm.toDomain(),
            items = items.toDomain(),
        )

    private fun List<ItemResponse>.toDomain(): List<BottariItem> = this.map { it.toDomain() }

    private fun ItemResponse.toDomain(): BottariItem =
        BottariItem(
            id = id.toLong(),
            name = name,
            isChecked = false,
        )
}
