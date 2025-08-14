package com.bottari.data.repository

import com.bottari.data.model.team.CreateTeamBottariRequest
import com.bottari.data.model.team.FetchTeamBottariChecklistResponse
import com.bottari.data.model.team.TeamMembersResponse
import com.bottari.data.source.remote.TeamBottariRemoteDataSource
import com.bottari.domain.model.member.Nickname
import com.bottari.domain.model.team.HeadCount
import com.bottari.domain.model.team.TeamBottariCheckList
import com.bottari.domain.model.team.TeamMembers
import com.bottari.domain.repository.TeamBottariRepository
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
            val request = CreateTeamBottariRequest(title)
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
            val request = CreateTeamBottariRequest(title)
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.createBottari(request) } returns Result.failure(exception)

            // when
            val result = repository.createTeamBottari(title)

            // then
            result shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.createBottari(request) }
        }

    @DisplayName("팀원 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamMembersReturnsSuccessTest() =
        runTest {
            // given
            val id = 1L
            val response = TeamMembersResponse("", 1, "test", listOf("test"))
            coEvery { dataSource.fetchTeamMembers(id) } returns Result.success(response)

            // when
            val result = repository.fetchTeamMembers(id)

            // then
            val expected =
                TeamMembers(
                    "",
                    HeadCount(1),
                    Nickname("test"),
                    listOf(Nickname("test")),
                )
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(expected)
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamMembers(id) }
        }

    @DisplayName("팀원 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamMembersReturnsFailureTest() =
        runTest {
            // given
            val id = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamMembers(id) } returns Result.failure(exception)

            // when
            val result = repository.fetchTeamMembers(id)

            // then
            result shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamMembers(id) }
        }

    @DisplayName("팀 체크리스트 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamChecklistReturnsSuccessTest() =
        runTest {
            // given
            val id = 1L
            val response = FetchTeamBottariChecklistResponse(listOf(), listOf(), listOf())
            coEvery { dataSource.fetchTeamBottari(id) } returns Result.success(response)

            // when
            val result = repository.fetchTeamBottari(id)

            // then
            val expected =
                TeamBottariCheckList(
                    sharedItems = listOf(),
                    assignedItems = listOf(),
                    personalItems = listOf(),
                )

            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(expected)
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottari(id) }
        }

    @DisplayName("팀 체크리스트 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamChecklistReturnsFailureTest() =
        runTest {
            // given
            val id = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamBottari(id) } returns Result.failure(exception)

            // when
            val result = repository.fetchTeamBottari(id)

            // then
            result shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottari(id) }
        }
}
