package com.bottari.data.testFixture

import com.bottari.data.model.item.FetchChecklistResponse

fun fetchChecklistResponseListFixture(): List<FetchChecklistResponse> =
    listOf(
        FetchChecklistResponse(id = 1, name = "item1", isChecked = true),
        FetchChecklistResponse(id = 2, name = "item2", isChecked = false),
    )
