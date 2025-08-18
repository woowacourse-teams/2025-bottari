package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamMemberUiModel(
    val id: Long?,
    val nickname: String,
    val isHost: Boolean,
) : Parcelable
