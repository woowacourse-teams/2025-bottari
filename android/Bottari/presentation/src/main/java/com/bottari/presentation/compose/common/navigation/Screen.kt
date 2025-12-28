package com.bottari.presentation.compose.common.navigation

import android.os.Parcelable
import androidx.compose.runtime.Stable

@Stable
interface Screen : Parcelable {
    val route: String
}
