package com.bottari.presentation.compose.common.navigation

import android.os.Parcelable

interface Screen : Parcelable {
    val route: String
}
