package com.bottari.presentation.compose.common.navigation

import android.os.Parcelable

interface Screen : Parcelable {
    val labelResId: Int
    val route: String
}
