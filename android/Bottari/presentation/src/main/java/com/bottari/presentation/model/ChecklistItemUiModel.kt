package com.bottari.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChecklistItemUiModel(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
) : Parcelable
