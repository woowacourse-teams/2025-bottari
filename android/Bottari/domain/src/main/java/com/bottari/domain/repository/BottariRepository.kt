package com.bottari.domain.repository

import com.bottari.domain.model.bottari.Bottari

interface BottariRepository {
    suspend fun fetchBottaries(ssaid: String): Result<List<Bottari>>
}
