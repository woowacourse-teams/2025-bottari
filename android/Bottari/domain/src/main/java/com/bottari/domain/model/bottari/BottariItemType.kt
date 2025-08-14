package com.bottari.domain.model.bottari

sealed interface BottariItemType {
    data object PERSONAL : BottariItemType

    data object SHARED : BottariItemType

    data class ASSIGNED(
        val members: List<String>,
    ) : BottariItemType
}
