package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bottari.config.JpaAuditingConfig;
import com.bottari.error.BusinessException;
import com.bottari.fcm.FcmMessageConverter;
import com.bottari.fcm.FcmMessageSender;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.CreateTeamBottariRequest;
import com.bottari.teambottari.dto.ReadTeamBottariPreviewResponse;
import com.bottari.teambottari.dto.ReadTeamBottariResponse;
import com.bottari.teambottari.dto.ReadTeamBottariResponse.TeamItem;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
@Import({
        TeamBottariService.class,
        JpaAuditingConfig.class,
        FcmMessageConverter.class
})
class TeamBottariServiceTest {

    @Autowired
    private TeamBottariService teamBottariService;

    @Autowired
    private EntityManager entityManager;

    @MockitoBean
    private FcmMessageSender fcmMessageSender;

    @Nested
    class GetAllBySsaid {

        @DisplayName("사용자의 팀 보따리 목록을 조회한다.")
        @Test
        void getAllBySsaid() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari1 = new TeamBottari("팀 보따리1", member, "code1");
            entityManager.persist(teamBottari1);
            final TeamMember teamMember1 = new TeamMember(teamBottari1, member);
            entityManager.persist(teamMember1);

            final TeamBottari teamBottari2 = new TeamBottari("팀 보따리2", member, "code2");
            entityManager.persist(teamBottari2);
            final TeamMember teamMember2 = new TeamMember(teamBottari2, member);
            entityManager.persist(teamMember2);
            entityManager.persist(teamBottari2);

            entityManager.flush();
            entityManager.clear();

            // when
            final List<ReadTeamBottariPreviewResponse> teamBottariPreviews =
                    teamBottariService.getAllBySsaid(member.getSsaid());

            // then
            assertThat(teamBottariPreviews)
                    .hasSize(2)
                    .extracting(ReadTeamBottariPreviewResponse::title)
                    .containsExactly("팀 보따리2", "팀 보따리1");
        }

        @DisplayName("사용자가 속한 팀 보따리 목록을 조회할 때, 팀 보따리에 속한 멤버 수를 포함한다.")
        @Test
        void getAllBySsaid_IncludesMemberCount() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = new TeamBottari("팀 보따리", member, "code");
            entityManager.persist(teamBottari);
            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);
            final TeamMember teamMember2 = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(teamMember2);

            entityManager.flush();
            entityManager.clear();

            // when
            final List<ReadTeamBottariPreviewResponse> teamBottariPreviews =
                    teamBottariService.getAllBySsaid(member.getSsaid());

            // then
            assertThat(teamBottariPreviews.getFirst().memberCount())
                    .isEqualTo(2);
        }

        @DisplayName("존재하지 않는 사용자로 팀 보따리 목록을 조회할 경우, 예외를 던진다.")
        @Test
        void getAllBySsaid_Exception_WhenNonExistentUser() {
            // given
            final String nonExistentSsaid = "non-existent-ssaid";

            // when & then
            assertThatThrownBy(() -> teamBottariService.getAllBySsaid(nonExistentSsaid))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }
    }

    @Nested
    class GetById {

        @DisplayName("팀 보따리를 ID로 조회한다.")
        @Test
        void getById() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = new TeamBottari("팀 보따리", member, "code");
            entityManager.persist(teamBottari);
            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);
            final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(anotherTeamMember);

            final TeamSharedItemInfo sharedItemInfo = new TeamSharedItemInfo("shared", teamBottari);
            entityManager.persist(sharedItemInfo);
            final TeamSharedItem sharedItem = new TeamSharedItem(sharedItemInfo, teamMember);
            entityManager.persist(sharedItem);
            final TeamSharedItem anotherSharedItem = new TeamSharedItem(sharedItemInfo, anotherTeamMember);
            entityManager.persist(anotherSharedItem);

            final TeamAssignedItemInfo assignedItemInfo = new TeamAssignedItemInfo("assigned", teamBottari);
            entityManager.persist(assignedItemInfo);
            final TeamAssignedItem assignedItem = new TeamAssignedItem(assignedItemInfo, anotherTeamMember);
            entityManager.persist(assignedItem);

            final TeamPersonalItem personalItem = new TeamPersonalItem("personal", teamMember);
            entityManager.persist(personalItem);
            final TeamPersonalItem anotherPersonalItem = new TeamPersonalItem("another_personal", anotherTeamMember);
            entityManager.persist(anotherPersonalItem);

            entityManager.flush();
            entityManager.clear();

            // when
            final Long teamBottariId = teamBottari.getId();
            final ReadTeamBottariResponse response = teamBottariService.getById(member.getSsaid(), teamBottariId);

            // then
            final ReadTeamBottariResponse expected = new ReadTeamBottariResponse(
                    teamBottari.getId(),
                    teamBottari.getTitle(),
                    List.of(
                            // 공통 물품이더라도 하나만 포함
                            new TeamItem(sharedItemInfo.getId(), sharedItem.getInfo().getName())
                    ),
                    List.of(
                            // 다른 사람에게 할당된 assigned item 포함
                            new TeamItem(assignedItemInfo.getId(), assignedItem.getInfo().getName())
                    ),
                    List.of(
                            // 본인에게 할당된 personal item 포함, 다른 사람의 personal item 제외
                            new TeamItem(personalItem.getId(), personalItem.getName())
                    ),
                    null // TODO: 알람 매핑 방향 의논 필요, 우선 null 반환
            );
            assertThat(response)
                    .isEqualTo(expected);
        }

        @DisplayName("존재하지 않는 사용자로 팀 보따리를 조회할 경우, 예외를 던진다.")
        @Test
        void getById_Exception_WhenNonExistentUser() {
            // given
            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);
            final TeamBottari teamBottari = new TeamBottari("팀 보따리", anotherMember, "code");
            entityManager.persist(teamBottari);
            final TeamMember teamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(teamMember);
            final Long teamBottariId = teamBottari.getId();

            final String nonExistentSsaid = "non-existent-ssaid";

            // when & then
            assertThatThrownBy(() -> teamBottariService.getById(nonExistentSsaid, teamBottariId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }

        @DisplayName("존재하지 않는 팀 보따리를 조회할 경우, 예외를 던진다.")
        @Test
        void getById_Exception_WhenNonExistentTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Long nonExistentTeamBottariId = 999L;

            // when & then
            assertThatThrownBy(() -> teamBottariService.getById(member.getSsaid(), nonExistentTeamBottariId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("소속된 팀 보따리를 조회할 때, 팀 멤버가 아닌 경우 예외를 던진다.")
        @Test
        void getById_Exception_WhenNotTeamMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = new TeamBottari("팀 보따리", member, "code");
            entityManager.persist(teamBottari);

            entityManager.flush();
            entityManager.clear();

            // when & then
            assertThatThrownBy(() -> teamBottariService.getById(member.getSsaid(), teamBottari.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    class CreateTest {

        @DisplayName("팀 보따리를 생성한다.")
        @Test
        void create() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            assertThat(teamBottariId).isNotNull();
        }

        @DisplayName("팀 보따리 생성 시, 팀 보따리의 초대 코드가 생성된다.")
        @Test
        void create_GeneratesInviteCode() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            final String inviteCode = entityManager.find(TeamBottari.class, teamBottariId)
                    .getInviteCode();
            assertThat(inviteCode).isNotBlank();
        }

        @DisplayName("팀 보따리 생성 시, 팀 보따리의 소유자가 올바르게 설정된다.")
        @Test
        void create_SetsOwnerCorrectly() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            final TeamBottari teamBottari = entityManager.find(TeamBottari.class, teamBottariId);
            assertThat(teamBottari.getOwner()).isEqualTo(member);
        }

        @DisplayName("팀 보따리 생성 시, 팀 멤버가 올바르게 생성된다.")
        @Test
        void create_CreatesTeamMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when
            final Long teamBottariId = teamBottariService.create(member.getSsaid(), request);

            // then
            final TeamBottari teamBottari = entityManager.find(TeamBottari.class, teamBottariId);
            final Long teamMemberCount = entityManager.createQuery(
                            "SELECT COUNT(tm) FROM TeamMember tm WHERE tm.teamBottari = :teamBottari", Long.class)
                    .setParameter("teamBottari", teamBottari)
                    .getSingleResult();
            assertThat(teamMemberCount).isEqualTo(1);
        }

        @DisplayName("존재하지 않는 사용자로 팀 보따리를 생성할 경우, 예외를 던진다.")
        @Test
        void create_Exception_WhenNonExistentUser() {
            // given
            final String nonExistentSsaid = "non-existent-ssaid";
            final CreateTeamBottariRequest request = new CreateTeamBottariRequest("title");

            // when & then
            assertThatThrownBy(() -> teamBottariService.create(nonExistentSsaid, request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("사용자를 찾을 수 없습니다. - 등록되지 않은 ssaid입니다.");
        }
    }

    @Nested
    class exitTest {

        @DisplayName("팀 보따리 탈퇴 시, 본인의 개인 물품들을 삭제한다.")
        @Test
        void exit_DeletePersonalItems() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            final TeamPersonalItem personalItem = new TeamPersonalItem("personalItem1", teamMember1);
            entityManager.persist(personalItem);
            final TeamPersonalItem otherPersonalItem = new TeamPersonalItem("personalItem2", teamMember2);
            entityManager.persist(otherPersonalItem);
            entityManager.flush();
            entityManager.clear();

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamPersonalItem actualMemberPersonalItem = (TeamPersonalItem) entityManager.createNativeQuery(
                    """
                     SELECT * 
                     FROM team_personal_item
                     WHERE id = :id
                     """, TeamPersonalItem.class)
                    .setParameter("id", personalItem.getId())
                    .getSingleResult();
            final TeamPersonalItem actualOtherMemberPersonalItem = (TeamPersonalItem) entityManager.createNativeQuery(
                    """
                      SELECT * 
                      FROM team_personal_item
                      WHERE id = :id
                      """, TeamPersonalItem.class)
                    .setParameter("id", otherPersonalItem.getId())
                    .getSingleResult();

            assertThat(actualMemberPersonalItem.getDeletedAt()).isNotNull();
            assertThat(actualOtherMemberPersonalItem.getDeletedAt()).isNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 본인의 공통 물품들을 삭제한다.")
        @Test
        void exit_DeleteSharedItems() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("sharedItem", teamBottari);
            entityManager.persist(teamSharedItemInfo);

            final TeamSharedItem sharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember1);
            entityManager.persist(sharedItem);
            final TeamSharedItem otherSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember2);
            entityManager.persist(otherSharedItem);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamSharedItem actualMemberSharedItem = (TeamSharedItem) entityManager.createNativeQuery(
                     """
                     SELECT * 
                     FROM team_shared_item
                     WHERE id = :id
                     """, TeamSharedItem.class)
                    .setParameter("id", sharedItem.getId())
                    .getSingleResult();
            final TeamSharedItem actualOtherMemberSharedItem = (TeamSharedItem) entityManager.createNativeQuery(
                            """
                                        SELECT * 
                                        FROM team_shared_item
                                        WHERE id = :id
                                    """, TeamSharedItem.class)
                    .setParameter("id", otherSharedItem.getId())
                    .getSingleResult();

            assertThat(actualMemberSharedItem.getDeletedAt()).isNotNull();
            assertThat(actualOtherMemberSharedItem.getDeletedAt()).isNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 본인의 담당 물품들을 삭제한다.")
        @Test
        void exit_DeleteAssignedItems() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("assignedInfo", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem assignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember1);
            entityManager.persist(assignedItem);
            final TeamAssignedItem otherAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember2);
            entityManager.persist(otherAssignedItem);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamAssignedItem actualMemberAssignedItem = (TeamAssignedItem) entityManager.createNativeQuery(
                   """
                    SELECT * 
                    FROM team_assigned_item
                    WHERE id = :id
                    """, TeamAssignedItem.class)
                    .setParameter("id", assignedItem.getId())
                    .getSingleResult();
            final TeamAssignedItem actualOtherMemberAssignedItem = (TeamAssignedItem) entityManager.createNativeQuery(
                            """
                                        SELECT * 
                                        FROM team_assigned_item
                                        WHERE id = :id
                                    """, TeamAssignedItem.class)
                    .setParameter("id", otherAssignedItem.getId())
                    .getSingleResult();

            assertThat(actualMemberAssignedItem.getDeletedAt()).isNotNull();
            assertThat(actualOtherMemberAssignedItem.getDeletedAt()).isNull();
        }

        @DisplayName("본인의 담당 물품들을 삭제 후, 해당 물품에 대해 담당하는 사람이 없을 경우 정보를 삭제한다.")
        @Test
        void exit_DeleteAssignedItemsInfo() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            final TeamAssignedItemInfo teamAssignedItemInfo1 = new TeamAssignedItemInfo("assignedInfo1", teamBottari);
            entityManager.persist(teamAssignedItemInfo1);
            final TeamAssignedItemInfo teamAssignedItemInfo2 = new TeamAssignedItemInfo("assignedInfo2", teamBottari);
            entityManager.persist(teamAssignedItemInfo2);

            final TeamAssignedItem assignedItem = new TeamAssignedItem(teamAssignedItemInfo1, teamMember1);
            entityManager.persist(assignedItem);
            final TeamAssignedItem otherAssignedItem = new TeamAssignedItem(teamAssignedItemInfo1, teamMember2);
            entityManager.persist(otherAssignedItem);
            final TeamAssignedItem assignedItem2 = new TeamAssignedItem(teamAssignedItemInfo2, teamMember1);
            entityManager.persist(assignedItem2);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamAssignedItemInfo actualAssignedItemInfo1 = (TeamAssignedItemInfo) entityManager.createNativeQuery(
                    """
                    SELECT * 
                    FROM team_assigned_item_info
                    WHERE id = :id
                    """, TeamAssignedItemInfo.class)
                    .setParameter("id", teamAssignedItemInfo1.getId())
                    .getSingleResult();
            final TeamAssignedItemInfo actualAssignedItemInfo2 = (TeamAssignedItemInfo) entityManager.createNativeQuery(
                    """
                    SELECT * 
                    FROM team_assigned_item_info
                    WHERE id = :id
                    """, TeamAssignedItemInfo.class)
                    .setParameter("id", teamAssignedItemInfo2.getId())
                    .getSingleResult();

            assertThat(actualAssignedItemInfo1.getDeletedAt()).isNull();
            assertThat(actualAssignedItemInfo2.getDeletedAt()).isNotNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 자신이 팀 보따리 주인인 경우 남은 인원 중 보따리에 들어온 순으로 주인을 넘긴다.")
        @Test
        void exit_ChangeOwner() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember1 = new Member("ssaid2", "member2");
            entityManager.persist(otherMember1);
            final Member otherMember2 = new Member("ssaid3", "member3");
            entityManager.persist(otherMember2);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember1);
            entityManager.persist(teamMember2);
            final TeamMember teamMember3 = new TeamMember(teamBottari, otherMember2);
            entityManager.persist(teamMember3);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            assertThat(teamBottari.getOwner().getId()).isEqualTo(otherMember1.getId());
        }

        @DisplayName("자신이 팀 보따리 주인이 아니고 남은 인원이 있는 경우, 팀 보따리 주인은 그대로 유지된다.")
        @Test
        void exit_NotChangeOwner() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember1 = new Member("ssaid2", "member2");
            entityManager.persist(otherMember1);
            final Member otherMember2 = new Member("ssaid3", "member3");
            entityManager.persist(otherMember2);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember1);
            entityManager.persist(teamMember2);
            final TeamMember teamMember3 = new TeamMember(teamBottari, otherMember2);
            entityManager.persist(teamMember3);

            // when
            teamBottariService.exit(teamBottari.getId(), otherMember1.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            assertThat(teamBottari.getOwner().getId()).isEqualTo(member.getId());
        }

        @DisplayName("팀 보따리 탈퇴 시, 팀 정보에서 삭제된다.")
        @Test
        void exit_DeleteTeamMember() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            // when
            teamBottariService.exit(teamBottari.getId(), otherMember.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamMember actualTeamMember1 = (TeamMember) entityManager.createNativeQuery(
                    """
                     SELECT * 
                     FROM team_member
                     WHERE id = :id
                     """, TeamMember.class)
                    .setParameter("id", teamMember1.getId())
                    .getSingleResult();
            final TeamMember actualTeamMember2 = (TeamMember) entityManager.createNativeQuery(
                            """
                                        SELECT * 
                                        FROM team_member
                                        WHERE id = :id
                                    """, TeamMember.class)
                    .setParameter("id", teamMember2.getId())
                    .getSingleResult();
            assertThat(actualTeamMember1.getDeletedAt()).isNull();
            assertThat(actualTeamMember2.getDeletedAt()).isNotNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 남은 인원이 없는 경우 공동 물품 정보를 모두 삭제한다.")
        @Test
        void exit_DeleteSharedItemInfo() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final TeamSharedItemInfo teamSharedItemInfo1 = new TeamSharedItemInfo("sharedItemInfo", teamBottari);
            entityManager.persist(teamSharedItemInfo1);
            final TeamSharedItemInfo teamSharedItemInfo2 = new TeamSharedItemInfo("sharedItemInfo", teamBottari);
            entityManager.persist(teamSharedItemInfo2);

            final TeamSharedItem teamSharedItem1 = new TeamSharedItem(teamSharedItemInfo1, teamMember);
            entityManager.persist(teamSharedItem1);
            final TeamSharedItem teamSharedItem2 = new TeamSharedItem(teamSharedItemInfo2, teamMember);
            entityManager.persist(teamSharedItem2);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamSharedItemInfo actualTeamSharedItemInfo1 = (TeamSharedItemInfo) entityManager.createNativeQuery(
                    """
                     SELECT * 
                     FROM team_shared_item_info
                     WHERE id = :id
                     """, TeamSharedItemInfo.class)
                    .setParameter("id", teamSharedItemInfo1.getId())
                    .getSingleResult();
            final TeamSharedItemInfo actualTeamSharedItemInfo2 = (TeamSharedItemInfo) entityManager.createNativeQuery(
                   """
                    SELECT * 
                    FROM team_shared_item_info
                    WHERE id = :id
                    """, TeamSharedItemInfo.class)
                    .setParameter("id", teamSharedItemInfo2.getId())
                    .getSingleResult();
            assertThat(actualTeamSharedItemInfo1.getDeletedAt()).isNotNull();
            assertThat(actualTeamSharedItemInfo2.getDeletedAt()).isNotNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 남은 인원이 있는 경우 공동 물품 정보는 유지된다.")
        @Test
        void exit_NotDeleteSharedItemInfo() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("sharedItemInfo", teamBottari);
            entityManager.persist(teamSharedItemInfo);

            final TeamSharedItem teamSharedItem1 = new TeamSharedItem(teamSharedItemInfo, teamMember1);
            entityManager.persist(teamSharedItem1);
            final TeamSharedItem teamSharedItem2 = new TeamSharedItem(teamSharedItemInfo, teamMember2);
            entityManager.persist(teamSharedItem2);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamSharedItemInfo actualTeamSharedItemInfo = (TeamSharedItemInfo) entityManager.createNativeQuery(
                    """
                     SELECT * 
                     FROM team_shared_item_info
                     WHERE id = :id
                     """, TeamSharedItemInfo.class)
                    .setParameter("id", teamSharedItemInfo.getId())
                    .getSingleResult();
            assertThat(actualTeamSharedItemInfo.getDeletedAt()).isNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 남은 인원이 없는 경우 팀 보따리를 삭제한다.")
        @Test
        void exit_DeleteTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamBottari actualTeamBottari = (TeamBottari) entityManager.createNativeQuery(
                     """
                     SELECT * 
                     FROM team_bottari
                     WHERE id = :id
                     """, TeamBottari.class)
                    .setParameter("id", teamBottari.getId())
                    .getSingleResult();
            assertThat(actualTeamBottari.getDeletedAt()).isNotNull();
        }

        @DisplayName("팀 보따리 탈퇴 시, 남은 인원이 있는 경우 팀 보따리를 삭제하지 않는다.")
        @Test
        void exit_NotDeleteTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);
            final Member otherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(otherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember1 = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember1);
            final TeamMember teamMember2 = new TeamMember(teamBottari, otherMember);
            entityManager.persist(teamMember2);

            // when
            teamBottariService.exit(teamBottari.getId(), member.getSsaid());
            entityManager.flush();
            entityManager.clear();

            // then
            final TeamBottari actualTeamBottari = (TeamBottari) entityManager.createNativeQuery(
                    """
                      SELECT * 
                      FROM team_bottari
                      WHERE id = :id
                      """, TeamBottari.class)
                    .setParameter("id", teamBottari.getId())
                    .getSingleResult();
            assertThat(actualTeamBottari.getDeletedAt()).isNull();
        }

        @DisplayName("팀 보따리 삭제 시, 팀 보따리가 존재하지 않은 경우 예외가 발생한다.")
        @Test
        void exit_Exception_NotExistTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long invalidTeamBottariId = 1L;

            // when & then
            assertThatThrownBy(() -> teamBottariService.exit(invalidTeamBottariId, member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("팀 보따리 삭제 시, 해당 팀에 속하지 않은 경우 예외가 발생한다.")
        @Test
        void exit_Exception_NotInTeam() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            // when & then
            assertThatThrownBy(() -> teamBottariService.exit(teamBottari.getId(), member.getSsaid()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }
}
