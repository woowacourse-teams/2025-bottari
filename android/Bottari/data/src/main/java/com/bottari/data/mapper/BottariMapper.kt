package com.bottari.data.mapper

import com.bottari.data.mapper.AlarmMapper.toDomain
import com.bottari.data.model.bottari.FetchBottariResponse
import com.bottari.data.model.bottari.FetchBottariesResponse
import com.bottari.data.model.bottari.ItemResponse
import com.bottari.domain.model.bottari.Bottari
import com.bottari.domain.model.bottari.BottariBase
import com.bottari.domain.model.bottari.BottariDetail
import com.bottari.domain.model.bottari.BottariItem
import com.bottari.domain.model.bottari.BottariItemType

object BottariMapper {
    fun FetchBottariesResponse.toDomain(): Bottari =
        Bottari(
            alarm = alarm?.toDomain(),
            checkedQuantity = checkedItemsCount,
            id = id,
            title = title,
            totalQuantity = totalItemsCount,
        )

    fun FetchBottariResponse.toDomain(): BottariDetail =
        BottariDetail(
            info =
                BottariBase(
                    id = id,
                    title = title,
                    alarm = alarm?.toDomain(),
                ),
            items = items.map { it.toDomain() },
        )

    private fun ItemResponse.toDomain(): BottariItem =
        BottariItem(
            id = id,
            name = name,
            type = BottariItemType.PERSONAL,
        )
}
