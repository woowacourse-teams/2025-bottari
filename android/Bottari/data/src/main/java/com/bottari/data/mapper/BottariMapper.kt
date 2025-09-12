package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.bottari.BottariFetchResponse
import com.bottari.data.model.bottari.BottariItemFetchResponse
import com.bottari.data.model.bottari.BottariesFetchResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariBase
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType

object BottariMapper {
    fun BottariesFetchResponse.toDomain(): Bottari =
        Bottari(
            base =
                BottariBase(
                    id = id,
                    title = title,
                    alarm = alarm?.toDomain(),
                ),
            checkedQuantity = checkedItemsCount,
            totalQuantity = totalItemsCount,
        )

    fun BottariFetchResponse.toDomain(): BottariDetail =
        BottariDetail(
            base =
                BottariBase(
                    id = id,
                    title = title,
                    alarm = alarm?.toDomain(),
                ),
            items = items.map { it.toDomain() },
        )

    private fun BottariItemFetchResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = BottariItemType.PERSONAL,
        )
}
