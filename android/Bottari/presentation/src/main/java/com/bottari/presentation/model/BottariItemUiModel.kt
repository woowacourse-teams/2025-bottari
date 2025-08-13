package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BottariItemUiModel(
    val id: Long,
    val name: String,
    val type: BottariItemTypeUiModel,
) : Parcelable
