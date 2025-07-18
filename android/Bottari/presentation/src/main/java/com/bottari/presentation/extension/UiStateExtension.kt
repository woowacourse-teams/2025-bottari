package com.bottari.presentation.extension

import com.bottari.presentation.base.UiState

fun <T> UiState<T>.takeSuccess() = (this as? UiState.Success)?.data
