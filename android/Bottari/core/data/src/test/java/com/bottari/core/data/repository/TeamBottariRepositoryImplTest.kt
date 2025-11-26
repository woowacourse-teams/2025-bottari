package com.bottari.core.data.repository

import com.bottari.core.data.fixture.TEAM_BOTTARI
import com.bottari.core.data.fixture.TEAM_BOTTARI_DETAIL
import com.bottari.core.data.fixture.TEAM_BOTTARI_DETAIL_RESPONSE
import com.bottari.core.data.fixture.TEAM_BOTTARI_RESPONSE
import com.bottari.core.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.core.domain.repository.TeamBottariRepository
import com.bottari.core.network.dto.team.bottari.TeamBottariCreateRequest
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

class TeamBottariRepositoryImplTest {
    private lateinit var dataSource: TeamBottariRemoteDataSource
    private lateinit var repository: TeamBottariRepository
    private val errorResponseBody =
        """{"message":"잘못된 요청입니다."}""".toResponseBody("application/json".toMediaType())

    @BeforeEach
    fun setUp() {
        dataSource = mockk<TeamBottariRemoteDataSource>()
        repository = TeamBottariRepositoryImpl(dataSource)
    }

    @DisplayName("팀 보따리 생성에 성공하면 Success를 반환한다")
    @Test
    fun createTeamBottariReturnsSuccessTest() =
        runTest {
            // given
            val title = "test"
            val id = 1L
            val request = TeamBottariCreateRequest(title)
            coEvery { dataSource.createBottari(request) } returns Result.success(id)

            // when
            val result = repository.createTeamBottari(title)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(id)
            }

            // verify
            coVerify(exactly = 1) { dataSource.createBottari(request) }
        }

    @DisplayName("팀 보따리 생성에 실패하면 Failure를 반환한다")
    @Test
    fun createTeamBottariReturnsFailureTest() =
        runTest {
            // given
            val title = "testtesttesttesttesttest"
            val request = TeamBottariCreateRequest(title)
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.createBottari(request) } returns Result.failure(exception)

            // when
            val result = repository.createTeamBottari(title)

            // then
            result shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.createBottari(request) }
        }

    @DisplayName("팀 보따리 목록 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamBottariReturnsSuccessTest() =
        runTest {
            // given
            val bottaries = listOf(TEAM_BOTTARI_RESPONSE, TEAM_BOTTARI_RESPONSE.copy(2L))
            coEvery { dataSource.fetchTeamBottaries() } returns Result.success(bottaries)

            // when
            val result = repository.fetchTeamBottaries()

            // then
            val expected =
                listOf(
                    TEAM_BOTTARI,
                    TEAM_BOTTARI.copy(id = 2),
                )
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(expected)
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottaries() }
        }

    @DisplayName("팀 보따리 목록 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamBottariReturnsFailureTest() =
        runTest {
            // given
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamBottaries() } returns Result.failure(exception)

            // when
            val result = repository.fetchTeamBottaries()

            // then
            result shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottaries() }
        }

    @DisplayName("팀 보따리 상세 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamBottariDetailReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            coEvery { dataSource.fetchTeamBottariDetail(teamBottariId) } returns
                Result.success(TEAM_BOTTARI_DETAIL_RESPONSE)

            // when
            val result = repository.fetchTeamBottariDetail(teamBottariId)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(TEAM_BOTTARI_DETAIL)
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottariDetail(teamBottariId) }
        }

    @DisplayName("팀 보따리 상세 조회에 실패하면 Failure 반환한다")
    @Test
    fun fetchTeamBottariDetailReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamBottariDetail(teamBottariId) } returns
                Result.failure(exception)

            // when
            val result = repository.fetchTeamBottariDetail(teamBottariId)

            // then
            result shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottariDetail(teamBottariId) }
        }

    @DisplayName("팀 보따리 나가기에 성공하면 Success를 반환한다")
    @Test
    fun exitTeamBottariReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            coEvery { dataSource.exitTeamBottari(teamBottariId) } returns Result.success(Unit)
            // when
            val result = repository.exitTeamBottari(teamBottariId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.exitTeamBottari(teamBottariId) }
        }

    @DisplayName("팀 보따리 나가기에 실패하면 Failure를 반환한다")
    @Test
    fun exitTeamBottariReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.exitTeamBottari(teamBottariId) } returns Result.failure(exception)

            // when
            val result = repository.exitTeamBottari(teamBottariId)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.exitTeamBottari(teamBottariId) }
        }
}
