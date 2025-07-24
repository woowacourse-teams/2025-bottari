package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottariDetailUiModel(
    val id: Long,
    val title: String,
    val alarm: AlarmUiModel?,
    val items: List<BottariItemUiModel> = emptyList(),
) : Parcelable
