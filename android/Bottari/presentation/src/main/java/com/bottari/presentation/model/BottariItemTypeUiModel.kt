package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface BottariItemTypeUiModel : Parcelable {
    @Parcelize
    data object PERSONAL : BottariItemTypeUiModel

    @Parcelize
    data object SHARED : BottariItemTypeUiModel

    @Parcelize
    data class ASSIGNED(
        val members: List<String> = emptyList(),
    ) : BottariItemTypeUiModel
}
