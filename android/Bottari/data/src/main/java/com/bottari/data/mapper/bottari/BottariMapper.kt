package com.bottari.data.mapper.bottari

import com.bottari.data.mapper.alarm.AlarmMapper.toAlarm
import com.bottari.data.mapper.bottari.item.BottariItemMapper.toBottariItem
import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariesFetchResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariState
import com.bottari.domain.model.bottari.item.BottariItemCount

object BottariMapper {
    fun BottariesFetchResponse.toBottariState(): BottariState =
        BottariState(
            bottari =
                Bottari(
                    id = id,
                    title = title,
                    alarm = alarm?.toAlarm(),
                    items = emptyList(),
                ),
            itemCount =
                BottariItemCount(
                    checkedQuantity = checkedItemsCount,
                    totalQuantity = totalItemsCount,
                ),
        )

    fun BottariFetchResponse.toBottari(): Bottari =
        Bottari(
            id = id,
            title = title,
            alarm = alarm?.toAlarm(),
            items = items.map { it.toBottariItem() },
        )
}
