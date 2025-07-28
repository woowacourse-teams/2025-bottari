package com.bottari.presentation.view.home.bottari.listener

interface OnBottariClickListener {
    fun onClick(
        bottariId: Long,
        bottariTitle: String,
    )

    fun onEditClick(bottariId: Long)

    fun onDeleteClick(bottariId: Long)
}
