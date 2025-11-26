package com.bottari.core.data.fixture

import com.bottari.core.network.dto.bottari.template.BottariTemplateFetchResponse

fun fetchBottariTemplateResponseListFixture(): List<BottariTemplateFetchResponse> =
    listOf(
        BottariTemplateFetchResponse(
            id = 1,
            title = "template1",
            description = "",
            items = emptyList(),
            author = "author",
            createdAt = "12:00",
            takenCount = 3,
            hashtags = emptyList(),
        ),
        BottariTemplateFetchResponse(
            id = 2,
            title = "template2",
            description = "",
            items = emptyList(),
            author = "author",
            createdAt = "13:00",
            takenCount = 4,
            hashtags = emptyList(),
        ),
    )
