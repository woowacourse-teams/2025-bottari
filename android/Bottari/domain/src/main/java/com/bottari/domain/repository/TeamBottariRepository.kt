package com.bottari.domain.repository

interface TeamBottariRepository {
    suspend fun createTeamBottari(title: String): Result<Long?>
}
