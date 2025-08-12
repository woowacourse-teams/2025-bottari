package com.bottari.teambottari.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import com.bottari.teambottari.dto.TeamMemberChecklistResponse;
import com.bottari.teambottari.dto.TeamMemberChecklistResponse.TeamMemberItemResponse;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TeamBottariItemService.class})
public class TeamBottariItemServiceTest {

    @Autowired
    private TeamBottariItemService teamMemberService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("체크리스트 정보를 가져온다.")
    @Test
    void getCheckList() {
        // given
        final Member member = MemberFixture.MEMBER.get();
        entityManager.persist(member);

        final TeamBottari teamBottari = TeamBottariFixture.TEAM_BOTTARI.get(member);
        entityManager.persist(teamBottari);

        final TeamMember teamMember = new TeamMember(teamBottari, member);
        entityManager.persist(teamMember);

        final TeamSharedItemInfo teamSharedItemInfo = new TeamSharedItemInfo("공통 물품", teamBottari);
        entityManager.persist(teamSharedItemInfo);

        final TeamSharedItem teamSharedItem = new TeamSharedItem(teamSharedItemInfo, teamMember);
        entityManager.persist(teamSharedItem);

        final TeamAssignedItemInfo teamAssignedItemInfo = new TeamAssignedItemInfo("담당 물품", teamBottari);
        entityManager.persist(teamAssignedItemInfo);

        final TeamAssignedItem teamAssignedItem = new TeamAssignedItem(teamAssignedItemInfo, teamMember);
        entityManager.persist(teamAssignedItem);

        final TeamPersonalItem teamPersonalItem = new TeamPersonalItem("개인 물품", teamMember);
        entityManager.persist(teamPersonalItem);

        // when
        final TeamMemberChecklistResponse actual = teamMemberService.getCheckList(teamBottari.getId(),
                member.getSsaid());

        // then
        final List<String> sharedItemNames = actual.sharedItems()
                .stream()
                .map(TeamMemberItemResponse::name)
                .toList();
        final List<String> assignedItemNames = actual.assignedItems()
                .stream()
                .map(TeamMemberItemResponse::name)
                .toList();
        final List<String> personalItemNames = actual.personalItems()
                .stream()
                .map(TeamMemberItemResponse::name)
                .toList();
        assertAll(
                () -> assertThat(sharedItemNames).containsExactly("공통 물품"),
                () -> assertThat(assignedItemNames).containsExactly("담당 물품"),
                () -> assertThat(personalItemNames).containsExactly("개인 물품")
        );
    }
}
