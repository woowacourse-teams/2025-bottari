package com.bottari.data.mapper.bottari

import com.bottari.data.mapper.alarm.AlarmMapper.toDomain
import com.bottari.data.mapper.bottari.item.BottariItemMapper.toDomain
import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariesFetchResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.bottari.item.BottariItemCount

object BottariMapper {
    fun BottariesFetchResponse.toDomain(): BottariState =
        BottariState(
            bottari =
                Bottari(
                    id = id,
                    title = title,
                    alarm = alarm?.toDomain(),
                    items = emptyList(),
                ),
            itemCount =
                BottariItemCount(
                    checkedQuantity = checkedItemsCount,
                    totalQuantity = totalItemsCount,
                ),
        )

    fun BottariFetchResponse.toDomain(): Bottari =
        Bottari(
            id = id,
            title = title,
            alarm = alarm?.toDomain(),
            items = items.map { it.toDomain() },
        )
}
