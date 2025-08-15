package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.fixture.MemberFixture;
import com.bottari.fixture.TeamBottariFixture;
import com.bottari.member.domain.Member;
import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import com.bottari.teambottari.dto.ReadTeamMemberInfoResponse;
import com.bottari.teambottari.dto.ReadTeamMemberNameResponse;
import com.bottari.teambottari.dto.ReadTeamMemberStatusResponse;
import com.bottari.teambottari.dto.TeamMemberItemResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TeamMemberService.class)
class TeamMemberServiceTest {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private EntityManager entityManager;


    @Nested
    class GetTeamMemberInfoByTeamBottariIdTest {

        @DisplayName("팀원 정보 및 팀원 초대 코드를 조회한다.")
        @Test
        void getTeamMemberInfoByTeamBottariId() {
            // given
            final Member owner = MemberFixture.MEMBER.get();
            entityManager.persist(owner);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(owner);
            entityManager.persist(teamBottari);

            final TeamMember ownerTeamMember = new TeamMember(teamBottari, owner);
            entityManager.persist(ownerTeamMember);

            final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(anotherTeamMember);

            // when
            final ReadTeamMemberInfoResponse actual = teamMemberService.getTeamMemberInfoByTeamBottariId(
                    teamBottari.getId(), anotherMember.getSsaid()
            );

            // then
            assertAll(
                    () -> assertThat(actual.inviteCode()).isEqualTo("inviteCode"),
                    () -> assertThat(actual.teamMemberCount()).isEqualTo(2),
                    () -> assertThat(actual.ownerName()).isEqualTo(owner.getName()),
                    () -> assertThat(actual.teamMemberNames()).containsExactlyInAnyOrder(
                            owner.getName(),
                            anotherMember.getName()
                    )
            );
        }

        @DisplayName("존재하지 않는 팀 보따리일 경우, 예외를 던진다.")
        @Test
        void getTeamMemberInfoByTeamBottariId_Exception_TeamBottariNotFound() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsTeamBottariId = 1L;

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberInfoByTeamBottariId(
                    notExistsTeamBottariId, member.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("해당 ssaid의 멤버가 팀 보따리에 속한 멤버가 아닐 경우, 예외를 던진다.")
        @Test
        void getTeamMemberInfoByTeamBottariId_Exception_MemberNotInTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberInfoByTeamBottariId(
                    teamBottari.getId(), anotherMember.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    class GetTeamMemberNameByTeamBottariId {

        @DisplayName("팀 보따리의 모든 팀원의 이름을 조회한다.")
        @Test
        void getTeamMemberNameByTeamBottariId() {
            // given
            final Member member1InTeamA = new Member("ssaid1", "nameA");
            final Member member2InTeamA = new Member("ssaid2", "nameB");
            final Member member3InTeamB = new Member("ssaid3", "nameC");
            entityManager.persist(member1InTeamA);
            entityManager.persist(member2InTeamA);
            entityManager.persist(member3InTeamB);

            final TeamBottari teamBottariA = new TeamBottari("title", member1InTeamA, "inviteCode1");
            final TeamBottari teamBottariB = new TeamBottari("title2", member3InTeamB, "inviteCode2");
            entityManager.persist(teamBottariA);
            entityManager.persist(teamBottariB);

            final TeamMember teamMember1 = new TeamMember(teamBottariA, member1InTeamA);
            final TeamMember teamMember2 = new TeamMember(teamBottariA, member2InTeamA);
            final TeamMember teamMember3 = new TeamMember(teamBottariB, member3InTeamB);
            entityManager.persist(teamMember1);
            entityManager.persist(teamMember2);
            entityManager.persist(teamMember3);

            final ReadTeamMemberNameResponse expectedMember1 = new ReadTeamMemberNameResponse(
                    member1InTeamA.getId(),
                    member1InTeamA.getName()
            );
            final ReadTeamMemberNameResponse expectedMember2 = new ReadTeamMemberNameResponse(
                    member2InTeamA.getId(),
                    member2InTeamA.getName()
            );

            // when
            final List<ReadTeamMemberNameResponse> memberNames = teamMemberService.getTeamMemberNameByTeamBottariId(
                    teamBottariA.getId(),
                    member1InTeamA.getSsaid()
            );

            // then
            assertThat(memberNames).contains(expectedMember1, expectedMember2);
        }

        @DisplayName("팀 보따리 맴버 이름 조회 시, 존재하지 않는 팀 보따리인 경우 예외가 발생한다.")
        @Test
        void getTeamMemberNameByTeamBottariId_Exception_TeamBottariNotFound() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsTeamBottariId = 1L;

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberNameByTeamBottariId(
                    notExistsTeamBottariId, member.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("팀 보따리 맴버 이름 조회 시, 해당 ssaid의 멤버가 팀 보따리에 속한 멤버가 아닐 경우, 예외를 던진다.")
        @Test
        void getTeamMemberNameByTeamBottariId_Exception_MemberNotInTeamBottari() {
            // given
            final Member member1InTeamA = new Member("ssaid1", "nameA");
            final Member member2InTeamB = new Member("ssaid3", "nameC");
            entityManager.persist(member1InTeamA);
            entityManager.persist(member2InTeamB);

            final TeamBottari teamBottariA = new TeamBottari("title", member1InTeamA, "inviteCode1");
            final TeamBottari teamBottariB = new TeamBottari("title2", member2InTeamB, "inviteCode2");
            entityManager.persist(teamBottariA);
            entityManager.persist(teamBottariB);

            final TeamMember teamMember1 = new TeamMember(teamBottariA, member1InTeamA);
            final TeamMember teamMember2 = new TeamMember(teamBottariB, member2InTeamB);
            entityManager.persist(teamMember1);
            entityManager.persist(teamMember2);

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberNameByTeamBottariId(
                    teamBottariA.getId(),
                    member2InTeamB.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }

    @Nested
    class GetTeamMemberStatusByTeamBottariId {

        @DisplayName("팀 보따리의 모든 팀원의 담당/물품 챙김 현황을 조회한다.")
        @Test
        void getTeamMemberStatusByTeamBottariId() {
            // given
            final Member owner = MemberFixture.MEMBER.get();
            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(owner);
            entityManager.persist(anotherMember);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(owner);
            entityManager.persist(teamBottari);

            final TeamMember ownerTeamMember = new TeamMember(teamBottari, owner);
            final TeamMember anotherTeamMember = new TeamMember(teamBottari, anotherMember);
            entityManager.persist(ownerTeamMember);
            entityManager.persist(anotherTeamMember);

            final TeamSharedItemInfo teamSharedItemInfo1 = new TeamSharedItemInfo("sharedItem1", teamBottari);
            final TeamSharedItemInfo teamSharedItemInfo2 = new TeamSharedItemInfo("sharedItem2", teamBottari);
            entityManager.persist(teamSharedItemInfo1);
            entityManager.persist(teamSharedItemInfo2);

            final TeamSharedItem ownerTeamMemberSharedItem1 = new TeamSharedItem(teamSharedItemInfo1, ownerTeamMember);
            ownerTeamMemberSharedItem1.check();
            final TeamSharedItem anotherTeamMemberSharedItem1 = new TeamSharedItem(
                    teamSharedItemInfo1,
                    anotherTeamMember
            );
            final TeamSharedItem ownerTeamMemberSharedItem2 = new TeamSharedItem(teamSharedItemInfo2, ownerTeamMember);
            final TeamSharedItem anotherTeamMemberSharedItem2 = new TeamSharedItem(
                    teamSharedItemInfo2,
                    anotherTeamMember
            );
            anotherTeamMemberSharedItem2.check();
            entityManager.persist(ownerTeamMemberSharedItem1);
            entityManager.persist(anotherTeamMemberSharedItem1);
            entityManager.persist(ownerTeamMemberSharedItem2);
            entityManager.persist(anotherTeamMemberSharedItem2);

            final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("assignedItem", teamBottari);
            entityManager.persist(teamAssignedItemInfo);

            final TeamAssignedItem anotherTeamMemberAssignedItem = new TeamAssignedItem(
                    teamAssignedItemInfo,
                    anotherTeamMember
            );
            anotherTeamMemberAssignedItem.check();
            entityManager.persist(anotherTeamMemberAssignedItem);

            // when
            final List<ReadTeamMemberStatusResponse> actual = teamMemberService.getTeamMemberStatusByTeamBottariId(
                    teamBottari.getId(),
                    anotherMember.getSsaid()
            );

            final ReadTeamMemberStatusResponse expectedElement1 = new ReadTeamMemberStatusResponse(
                    owner.getName(),
                    true,
                    2,
                    1,
                    List.of(
                            TeamMemberItemResponse.from(ownerTeamMemberSharedItem1),
                            TeamMemberItemResponse.from(ownerTeamMemberSharedItem2)
                    ),
                    List.of()
            );
            final ReadTeamMemberStatusResponse expectedElement2 = new ReadTeamMemberStatusResponse(
                    anotherMember.getName(),
                    false,
                    3,
                    2,
                    List.of(
                            TeamMemberItemResponse.from(anotherTeamMemberSharedItem1),
                            TeamMemberItemResponse.from(anotherTeamMemberSharedItem2)
                    ),
                    List.of(TeamMemberItemResponse.from(anotherTeamMemberAssignedItem))
            );

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual).containsExactly(expectedElement1, expectedElement2)
            );
        }

        @DisplayName("존재하지 않는 팀 보따리일 경우, 예외를 던진다.")
        @Test
        void getTeamMemberStatusByTeamBottariId_Exception_TeamBottariNotFound() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final Long notExistsTeamBottariId = 1L;

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberStatusByTeamBottariId(
                    notExistsTeamBottariId, member.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("팀 보따리를 찾을 수 없습니다.");
        }

        @DisplayName("해당 ssaid의 멤버가 팀 보따리에 속한 멤버가 아닐 경우, 예외를 던진다.")
        @Test
        void getTeamMemberStatusByTeamBottariId_Exception_MemberNotInTeamBottari() {
            // given
            final Member member = MemberFixture.MEMBER.get();
            entityManager.persist(member);

            final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
            entityManager.persist(teamBottari);

            final TeamMember teamMember = new TeamMember(teamBottari, member);
            entityManager.persist(teamMember);

            final Member anotherMember = MemberFixture.ANOTHER_MEMBER.get();
            entityManager.persist(anotherMember);

            // when & then
            assertThatThrownBy(() -> teamMemberService.getTeamMemberStatusByTeamBottariId(
                    teamBottari.getId(), anotherMember.getSsaid()
            )).isInstanceOf(BusinessException.class)
                    .hasMessage("해당 팀 보따리의 팀 멤버가 아닙니다.");
        }
    }
}
