package com.bottari.core.data.repository

import com.bottari.core.data.fixture.BOTTARI_ASSIGNED_ITEM_FIXTURE
import com.bottari.core.data.fixture.BOTTARI_ASSIGNED_ITEM_RESPONSE_FIXTURE
import com.bottari.core.data.fixture.BOTTARI_PERSONAL_ITEM_FIXTURE
import com.bottari.core.data.fixture.BOTTARI_PERSONAL_ITEM_RESPONSE_FIXTURE
import com.bottari.core.data.fixture.BOTTARI_SHARED_ITEM_FIXTURE
import com.bottari.core.data.fixture.BOTTARI_SHARED_ITEM_RESPONSE_FIXTURE
import com.bottari.core.data.fixture.SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE
import com.bottari.core.data.source.remote.TeamBottariItemsRemoteDataSource
import com.bottari.core.domain.model.team.bottari.TeamBottariCheckList
import com.bottari.core.domain.model.team.bottari.item.TeamBottariItemType
import com.bottari.core.domain.repository.TeamBottariItemsRepository
import com.bottari.core.network.dto.team.bottari.item.AssignedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.PersonalItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.SharedItemsCreateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemCheckUpdateRequest
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemChecklistFetchResponse
import com.bottari.core.network.dto.team.bottari.item.TeamBottariItemDeleteRequest
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

class TeamBottariItemsRepositoryImplTest {
    private lateinit var dataSource: TeamBottariItemsRemoteDataSource
    private lateinit var repository: TeamBottariItemsRepository
    private val errorResponseBody =
        """{"message":"잘못된 요청입니다."}""".toResponseBody("application/json".toMediaType())

    @BeforeEach
    fun setUp() {
        dataSource = mockk<TeamBottariItemsRemoteDataSource>()
        repository = TeamBottariItemsRepositoryImpl(dataSource)
    }

    @DisplayName("팀 체크리스트 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamChecklistReturnsSuccessTest() =
        runTest {
            // given
            val id = 1L
            val response = TeamBottariItemChecklistFetchResponse(listOf(), listOf(), listOf())
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

    @DisplayName("팀 보따리 개인 아이템 삭제에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamBottariPersonalItemReturnsSuccessTest() =
        runTest {
            // given
            val id = 1L
            val teamBottariItemType = TeamBottariItemType.PERSONAL
            val request = TeamBottariItemDeleteRequest(teamBottariItemType.toString())
            coEvery {
                dataSource.deleteTeamBottariItem(
                    id,
                    request,
                )
            } returns Result.success(Unit)

            // when
            val result = repository.deleteTeamBottariItem(id, teamBottariItemType)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) { dataSource.deleteTeamBottariItem(id, request) }
        }

    @DisplayName("팀 보따리 개인 아이템 삭제에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamBottariPersonalItemReturnsFailureTest() =
        runTest {
            // given
            val id = 1L
            val teamBottariItemType = TeamBottariItemType.PERSONAL
            val request = TeamBottariItemDeleteRequest(teamBottariItemType.toString())
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                dataSource.deleteTeamBottariItem(
                    id,
                    request,
                )
            } returns Result.failure(exception)

            // when
            val result = repository.deleteTeamBottariItem(id, teamBottariItemType)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.deleteTeamBottariItem(id, request) }
        }

    @DisplayName("팀 보따리 개인 물건을 추가에 성공하면 Success를 반환한다")
    @Test
    fun createTeamBottariPersonalItemReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemName = "item"
            coEvery {
                dataSource.createTeamBottariPersonalItem(
                    teamBottariId,
                    PersonalItemsCreateRequest(itemName),
                )
            } returns Result.success(Unit)

            // when
            val result = repository.createTeamBottariPersonalItem(teamBottariId, itemName)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) {
                dataSource.createTeamBottariPersonalItem(
                    teamBottariId,
                    PersonalItemsCreateRequest(itemName),
                )
            }
        }

    @DisplayName("팀 보따리 개인 물건을 추가에 실패하면 Failure를 반환한다")
    @Test
    fun createTeamBottariPersonalItemReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemName = "item"

            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                dataSource.createTeamBottariPersonalItem(
                    teamBottariId,
                    PersonalItemsCreateRequest(itemName),
                )
            } returns Result.failure(exception)

            // when
            val result = repository.createTeamBottariPersonalItem(teamBottariId, itemName)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) {
                dataSource.createTeamBottariPersonalItem(
                    teamBottariId,
                    PersonalItemsCreateRequest(itemName),
                )
            }
        }

    @DisplayName("팀 보따리 공통 물건을 추가에 성공하면 Success를 반환한다")
    @Test
    fun createTeamBottariSharedItemReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemName = "item"

            coEvery {
                dataSource.createTeamBottariSharedItem(
                    teamBottariId,
                    SharedItemsCreateRequest(itemName),
                )
            } returns Result.success(Unit)

            // when
            val result = repository.createTeamBottariSharedItem(teamBottariId, itemName)

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) {
                dataSource.createTeamBottariSharedItem(
                    teamBottariId,
                    SharedItemsCreateRequest(itemName),
                )
            }
        }

    @DisplayName("팀 보따리 공통 물건을 추가에 실패하면 Failure를 반환한다")
    @Test
    fun createTeamBottariSharedItemReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemName = "item"

            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                dataSource.createTeamBottariSharedItem(
                    teamBottariId,
                    SharedItemsCreateRequest(itemName),
                )
            } returns Result.failure(exception)

            // when
            val result = repository.createTeamBottariSharedItem(teamBottariId, itemName)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) {
                dataSource.createTeamBottariSharedItem(
                    teamBottariId,
                    SharedItemsCreateRequest(itemName),
                )
            }
        }

    @DisplayName("팀 보따리 담당 물건을 추가에 성공하면 Success를 반환한다")
    @Test
    fun createTeamBottariAssignedItemReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemName = "item"

            coEvery {
                dataSource.createTeamBottariAssignedItem(
                    teamBottariId,
                    AssignedItemsCreateRequest(itemName, listOf(1L)),
                )
            } returns Result.success(Unit)

            // when
            val result =
                repository.createTeamBottariAssignedItem(teamBottariId, itemName, listOf(1L))

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) {
                dataSource.createTeamBottariAssignedItem(
                    teamBottariId,
                    AssignedItemsCreateRequest(itemName, listOf(1L)),
                )
            }
        }

    @DisplayName("팀 보따리 담당 물건을 추가에 실패하면 Failure를 반환한다")
    @Test
    fun createTeamBottariAssignedItemReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemName = "item"

            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                dataSource.createTeamBottariAssignedItem(
                    teamBottariId,
                    AssignedItemsCreateRequest(itemName, listOf(1L)),
                )
            } returns Result.failure(exception)

            // when
            val result =
                repository.createTeamBottariAssignedItem(teamBottariId, itemName, listOf(1L))

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) {
                dataSource.createTeamBottariAssignedItem(
                    teamBottariId,
                    AssignedItemsCreateRequest(itemName, listOf(1L)),
                )
            }
        }

    @DisplayName("팀 보따리 개인 물건 목록 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamPersonalItemsReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L

            val personalItems = listOf(BOTTARI_PERSONAL_ITEM_RESPONSE_FIXTURE)
            coEvery { dataSource.fetchTeamPersonalItems(teamBottariId) } returns
                Result.success(
                    personalItems,
                )

            // when
            val result = repository.fetchTeamPersonalItems(teamBottariId)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(listOf(BOTTARI_PERSONAL_ITEM_FIXTURE))
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamPersonalItems(teamBottariId) }
        }

    @DisplayName("팀 보따리 개인 물건 목록 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamPersonalItemsReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))

            coEvery { dataSource.fetchTeamPersonalItems(teamBottariId) } returns
                Result.failure(
                    exception,
                )

            // when
            val result = repository.fetchTeamPersonalItems(teamBottariId)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamPersonalItems(teamBottariId) }
        }

    @DisplayName("팀 보따리 공통 물건 목록 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamSharedItemsReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val sharedItems = listOf(BOTTARI_SHARED_ITEM_RESPONSE_FIXTURE)
            coEvery { dataSource.fetchTeamSharedItems(teamBottariId) } returns
                Result.success(
                    sharedItems,
                )

            // when
            val result = repository.fetchTeamSharedItems(teamBottariId)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(listOf(BOTTARI_SHARED_ITEM_FIXTURE))
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamSharedItems(teamBottariId) }
        }

    @DisplayName("팀 보따리 공통 물건 목록 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamSharedItemsReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamSharedItems(teamBottariId) } returns
                Result.failure(
                    exception,
                )

            // when
            val result = repository.fetchTeamSharedItems(teamBottariId)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamSharedItems(teamBottariId) }
        }

    @DisplayName("팀 보따리 담당 물건 목록 조회에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamAssignedItemsReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val assignedItems = listOf(BOTTARI_ASSIGNED_ITEM_RESPONSE_FIXTURE)
            coEvery { dataSource.fetchTeamAssignedItems(teamBottariId) } returns
                Result.success(
                    assignedItems,
                )

            // when
            val result = repository.fetchTeamAssignedItems(teamBottariId)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(listOf(BOTTARI_ASSIGNED_ITEM_FIXTURE))
            }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamAssignedItems(teamBottariId) }
        }

    @DisplayName("팀 보따리 담당 물건 목록 조회에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamAssignedItemsReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery { dataSource.fetchTeamAssignedItems(teamBottariId) } returns
                Result.failure(
                    exception,
                )

            // when
            val result = repository.fetchTeamAssignedItems(teamBottariId)

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) { dataSource.fetchTeamAssignedItems(teamBottariId) }
        }

    @DisplayName("팀 보따리 담당 물건 수정에 성공하면 Success를 반환한다")
    @Test
    fun saveTeamBottariAssignedItemReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemId = 1L
            val newName = "new name"

            coEvery {
                dataSource.saveTeamBottariAssignedItem(
                    teamBottariId,
                    itemId,
                    SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE,
                )
            } returns Result.success(Unit)

            // when
            val result =
                repository.saveTeamBottariAssignedItem(
                    teamBottariId,
                    itemId,
                    newName,
                    listOf(1L, 2L),
                )

            // then
            result.shouldBeSuccess()

            // verify
            coVerify(exactly = 1) {
                dataSource.saveTeamBottariAssignedItem(
                    teamBottariId,
                    itemId,
                    SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE,
                )
            }
        }

    @DisplayName("팀 보따리 담당 물건 수정에 실패하면 Failure를 반환한다")
    @Test
    fun saveTeamBottariAssignedItemReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val itemId = 1L
            val newName = "new name"

            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))

            coEvery {
                dataSource.saveTeamBottariAssignedItem(
                    teamBottariId,
                    itemId,
                    SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE,
                )
            } returns Result.failure(exception)

            // when
            val result =
                repository.saveTeamBottariAssignedItem(
                    teamBottariId,
                    itemId,
                    newName,
                    listOf(1L, 2L),
                )

            // then
            result.shouldBeFailure { error -> error shouldBe exception }

            // verify
            coVerify(exactly = 1) {
                dataSource.saveTeamBottariAssignedItem(
                    teamBottariId,
                    itemId,
                    SAVE_TEAM_BOTTARI_ASSIGNED_ITEM_REQUEST_FIXTURE,
                )
            }
        }

    @DisplayName("팀 보따리 물건 체크에 성공하면 Success를 반환한다")
    @Test
    fun fetchTeamCheckBottariItemsReturnsSuccessTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val type = "PERSONAL"
            coEvery {
                dataSource.checkBottariItem(
                    teamBottariId,
                    TeamBottariItemCheckUpdateRequest(type),
                )
            } returns Result.success(Unit)

            // when
            val result = repository.checkBottariItem(teamBottariId, type)

            // then
            assertSoftly(result) {
                shouldBeSuccess()
                getOrThrow().shouldBe(Unit)
            }

            // verify
            coVerify(exactly = 1) {
                dataSource.checkBottariItem(
                    teamBottariId,
                    TeamBottariItemCheckUpdateRequest(type),
                )
            }
        }

    @DisplayName("팀 보따리 물건 체크에 실패하면 Failure를 반환한다")
    @Test
    fun fetchTeamCheckBottariItemsReturnsFailureTest() =
        runTest {
            // given
            val teamBottariId = 1L
            val type = "ERROR"
            val exception = HttpException(Response.error<Unit>(400, errorResponseBody))
            coEvery {
                dataSource.checkBottariItem(
                    teamBottariId,
                    TeamBottariItemCheckUpdateRequest(type),
                )
            } returns Result.failure(exception)

            // when
            val result = repository.checkBottariItem(teamBottariId, type)

            // then
            assertSoftly(result) {
                result.shouldBeFailure { error -> error shouldBe exception }
            }

            // verify
            coVerify(exactly = 1) {
                dataSource.checkBottariItem(
                    teamBottariId,
                    TeamBottariItemCheckUpdateRequest(type),
                )
            }
        }
}
