package com.bottari.presentation.model.bottari

import android.os.Parcelable
import com.bottari.domain.model.bottari.item.BottariItem
import com.bottari.domain.model.bottari.item.ChecklistItem
import com.bottari.presentation.model.bottari.personal.BottariItemTypeUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottariItemUiModel(
    val id: Long,
    val name: String,
    val type: BottariItemTypeUiModel,
) : Parcelable {
    companion object {
        fun fromDomain(bottariItem: BottariItem): BottariItemUiModel =
            BottariItemUiModel(
                id = bottariItem.id,
                name = bottariItem.name,
                type = BottariItemTypeUiModel.fromDomain(bottariItem.type),
            )

        fun fromDomain(bottariItem: ChecklistItem): BottariItemUiModel =
            BottariItemUiModel(
                id = bottariItem.id,
                name = bottariItem.name,
                type = BottariItemTypeUiModel.PERSONAL,
            )
    }
}
