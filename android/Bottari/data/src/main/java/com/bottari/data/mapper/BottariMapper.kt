package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariItemFetchResponse
import com.bottari.data.model.bottari.BottariesFetchResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType
import com.bottari.domain.model.bottari.BottariState

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
            checkedQuantity = checkedItemsCount,
            totalQuantity = totalItemsCount,
        )

    fun BottariFetchResponse.toDomain(): Bottari =
        Bottari(
            id = id,
            title = title,
            alarm = alarm?.toDomain(),
            items = items.map { it.toDomain() },
        )

    private fun BottariItemFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = BottariItemType.PERSONAL,
        )
}
