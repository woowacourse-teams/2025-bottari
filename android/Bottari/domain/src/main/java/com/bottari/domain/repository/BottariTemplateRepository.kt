package com.bottari.domain.repository

import com.bottari.domain.model.template.BottariTemplate

interface BottariTemplateRepository {
    suspend fun fetchBottariTemplates(): Result<List<BottariTemplate>>
}
