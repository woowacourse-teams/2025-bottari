package com.bottari.presentation.view.home.bottari.listener

interface OnBottariClickListener {
    fun onClick(
        bottariId: Long,
        bottariTitle: String,
    )

    fun onMoreClick(bottariId: Long)
}
