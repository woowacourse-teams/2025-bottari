package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.bottari.BottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.ItemResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem

object BottariMapper {
    fun FetchBottariesResponse.toDomain(): Bottari =
        Bottari(
            alarm = alarmResponse?.toDomain(),
            checkedQuantity = checkedItemsCount,
            id = id,
            title = title,
            totalQuantity = totalItemsCount,
        )

    fun BottariResponse.toDomain(): BottariDetail =
        BottariDetail(
            id = id,
            title = title,
            alarm = alarm?.toDomain(),
            items = items.map { it.toDomain() },
        )

    private fun ItemResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            isChecked = false,
        )
}
