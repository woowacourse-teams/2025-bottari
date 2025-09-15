package com.bottari.data.testFixture

import com.bottari.data.model.bottari.item.ItemFetchResponse

fun fetchChecklistResponseListFixture(): List<ItemFetchResponse> =
    listOf(
        ItemFetchResponse(id = 1, name = "item1", isChecked = true),
        ItemFetchResponse(id = 2, name = "item2", isChecked = false),
    )
