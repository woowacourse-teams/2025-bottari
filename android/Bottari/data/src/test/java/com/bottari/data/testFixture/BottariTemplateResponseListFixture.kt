package com.bottari.data.testFixture

import com.bottari.data.model.template.FetchBottariTemplateResponse

fun fetchBottariTemplateResponseListFixture(): List<FetchBottariTemplateResponse> =
    listOf(
        FetchBottariTemplateResponse(
            id = 1,
            title = "template1",
            items = emptyList(),
            author = "author",
            createdAt = "12:00",
            takenCount = 3,
        ),
        FetchBottariTemplateResponse(
            id = 2,
            title = "template2",
            items = emptyList(),
            author = "author",
            createdAt = "13:00",
            takenCount = 4,
        ),
    )
