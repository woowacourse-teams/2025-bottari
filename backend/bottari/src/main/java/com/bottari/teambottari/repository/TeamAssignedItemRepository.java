package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamAssignedItemRepository extends JpaRepository<TeamAssignedItem, Long> {

    @Query("""
            SELECT tai
            FROM TeamAssignedItem tai
            JOIN FETCH tai.info
            JOIN FETCH tai.teamMember tm
            JOIN FETCH tm.member
            WHERE tai.teamMember.teamBottari.id = :teamBottariId
            """)
    List<TeamAssignedItem> findAllByTeamBottariId(final Long teamBottariId);

    List<TeamAssignedItem> findAllByTeamMemberIn(final List<TeamMember> teamMembers);

    @Query("""
            SELECT tai
            FROM TeamAssignedItem tai
            JOIN FETCH tai.info
            WHERE tai.teamMember.id = :teamMemberId
            """)
    List<TeamAssignedItem> findAllByTeamMemberId(final Long teamMemberId);

    @Query("""
            SELECT tai
            FROM TeamAssignedItem tai
            JOIN FETCH TeamBottari tb ON  tai.info.teamBottari.id = :infoId
            JOIN FETCH tai.teamMember tm
            JOIN FETCH tm.member
            WHERE tai.info.id = :infoId
            """)
    List<TeamAssignedItem> findAllByInfoIdWithMember(Long infoId);
}
