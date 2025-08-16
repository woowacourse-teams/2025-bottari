package com.bottari.presentation.view.edit.team.item.main.adapter

import androidx.fragment.app.Fragment

data class TabInfo(
    val titleRes: Int,
    val fragmentProvider: (Long) -> Fragment,
)
