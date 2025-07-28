package com.bottari.data.testFixture

import com.bottari.data.model.template.FetchBottariTemplateResponse

fun fetchBottariTemplateResponseListFixture(): List<FetchBottariTemplateResponse> =
    listOf(
        FetchBottariTemplateResponse(id = 1, title = "template1", items = emptyList(), author = "author"),
        FetchBottariTemplateResponse(id = 2, title = "template2", items = emptyList(), author = "author"),
    )
