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
        val members: List<TeamMemberUiModel> = emptyList(),
    ) : BottariItemTypeUiModel

    fun toTypeString() =
        when (this) {
            is PERSONAL -> "PERSONAL"
            is SHARED -> "SHARED"
            is ASSIGNED -> "ASSIGNED"
        }
}
