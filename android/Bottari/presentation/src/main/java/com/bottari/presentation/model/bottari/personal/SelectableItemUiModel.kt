package com.bottari.presentation.model.bottari.personal

import android.os.Parcelable
import com.bottari.domain.model.bottari.item.BottariItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectableItemUiModel(
    val id: Long,
    val name: String,
    val type: BottariItemTypeUiModel,
    val isSelected: Boolean = false,
) : Parcelable {
    companion object {
        fun fromDomain(bottariItem: BottariItem): SelectableItemUiModel =
            SelectableItemUiModel(
                id = bottariItem.id,
                name = bottariItem.name,
                type = BottariItemTypeUiModel.fromDomain(bottariItem.type),
            )
    }
}
