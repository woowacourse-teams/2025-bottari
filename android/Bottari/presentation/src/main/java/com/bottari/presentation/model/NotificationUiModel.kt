package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationUiModel(
    val id: Long,
    val title: String,
    val alarm: AlarmUiModel,
) : Parcelable
