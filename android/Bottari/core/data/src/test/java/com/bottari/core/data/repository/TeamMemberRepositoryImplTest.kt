package com.bottari.core.data.repository

import com.bottari.core.data.fixture.TEAM_MEMBER
import com.bottari.core.data.fixture.TEAM_MEMBERS_STATUS
import com.bottari.core.data.fixture.TEAM_MEMBERS_STATUS_RESPONSE
import com.bottari.core.data.fixture.TEAM_MEMBER_RESPONSE
import com.bottari.core.data.source.remote.TeamMemberRemoteDataSource
import com.bottari.core.domain.model.member.Nickname
import com.bottari.core.domain.model.team.member.HeadCount
import com.bottari.core.domain.model.team.member.TeamStatus
import com.bottari.core.domain.repository.TeamMemberRepository
import com.bottari.core.network.dto.team.bottari.TeamBottariJoinRequest
import com.bottari.core.network.dto.team.member.TeamMemberFetchResponse
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

class TeamMemberRepositoryImplTest {
    private lateinit var dataSource: TeamMemberRemoteDataSource

    private lateinit var repository: TeamMemberRepository

    private val errorResponseBody =
        """{"message":"잘못된 요청입니다."}""".toResponseBody("application/json".toMediaType())

    @BeforeEach
    fun setUp() {
        dataSource = mockk<TeamMemberRemoteDataSource>()
        repository = TeamMemberRepositoryImpl(dataSource)
    }

    @DisplayName("팀원 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamMembersReturnsSuccessTest() =
        runTest {
            // given
            val id = 1L
            val response = TeamMemberFetchResponse("", 1, "test", listOf("test"))
            coEvery { dataSource.fetchTeamMembers(id) } returns Result.success(response)

            // when
            val result = repository.fetchTeamMembers(id)

            // then
            val expected =
                TeamStatus(
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

    @DisplayName("팀 멤버 현황 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamMembersStatusReturnsSuccessTest() =
        runTest {
            // given
            val id = 1L
            coEvery { dataSource.fetchTeamMembersStatus(id) } returns
                Result.success(
                    TEAM_MEMBERS_STATUS_RESPONSE,
                )

            // when
            val result = repository.fetchTeamMembersStatus(id)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(TEAM_MEMBERS_STATUS)
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamMembersStatus(id) }
        }

    @DisplayName("팀 멤버 현황 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamMembersStatusReturnsFailureTest() =
        runTest {
            // given
            val id = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamMembersStatus(id) } returns Result.failure(exception)

            // when
            val result = repository.fetchTeamMembersStatus(id)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamMembersStatus(id) }
        }

    @DisplayName("보채기 알림 전송에 성공하면 Success를 반환한다")
    @Test
    fun sendRemindByMemberMessageReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val memberId = 1L
            coEvery {
                dataSource.sendRemindByMemberMessage(
                    teamBottariId,
                    memberId,
                )
            } returns Result.success(Unit)

            // when
            val result = repository.sendRemindByMemberMessage(teamBottariId, memberId)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) {
                dataSource.sendRemindByMemberMessage(teamBottariId, memberId)
            }
        }

    @DisplayName("보채기 알림 전송에 실패하면 Failure를 반환한다")
    @Test
    fun sendRemindByMemberMessageReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val memberId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                dataSource.sendRemindByMemberMessage(
                    teamBottariId,
                    memberId,
                )
            } returns Result.failure(exception)

            // when
            val result = repository.sendRemindByMemberMessage(teamBottariId, memberId)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) {
                dataSource.sendRemindByMemberMessage(teamBottariId, memberId)
            }
        }

    @DisplayName("팀 보따리 참가에 성공하면 Success를 반환한다")
    @Test
    fun joinTeamBottariReturnsSuccessTest() =
        runTest {
            // given
            val inviteCode = "TEST123"
            val request = TeamBottariJoinRequest(inviteCode)
            coEvery { dataSource.joinTeamBottari(request) } returns Result.success(Unit)

            // when
            val result = repository.joinTeamBottari(inviteCode)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.joinTeamBottari(request) }
        }

    @DisplayName("팀 보따리 참가에 실패하면 Failure를 반환한다")
    @Test
    fun joinTeamBottariReturnsFailureTest() =
        runTest {
            // given
            val inviteCode = "TEST123"
            val request = TeamBottariJoinRequest(inviteCode)
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.joinTeamBottari(request) } returns Result.failure(exception)

            // when
            val result = repository.joinTeamBottari(inviteCode)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.joinTeamBottari(request) }
        }

    @DisplayName("팀 보따리 멤버 목록 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamBottariMembersReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val membersResponse =
                listOf(TEAM_MEMBER_RESPONSE, TEAM_MEMBER_RESPONSE.copy(2L, "member2"))
            coEvery { dataSource.fetchTeamBottariMembers(teamBottariId) } returns
                Result.success(
                    membersResponse,
                )

            // when
            val result = repository.fetchTeamBottariMembers(teamBottariId)

            // then
            val expected = listOf(TEAM_MEMBER, TEAM_MEMBER.copy(2L, "member2"))
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(expected)
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottariMembers(teamBottariId) }
        }

    @DisplayName("팀 보따리 멤버 목록 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamBottariMembersReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamBottariMembers(teamBottariId) } returns
                Result.failure(
                    exception,
                )

            // when
            val result = repository.fetchTeamBottariMembers(teamBottariId)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamBottariMembers(teamBottariId) }
        }
}
