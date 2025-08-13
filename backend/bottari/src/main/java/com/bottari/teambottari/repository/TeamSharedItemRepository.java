package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamSharedItemRepository extends JpaRepository<TeamSharedItem, Long> {

    @Query("""
            SELECT tsi
            FROM TeamSharedItem tsi
            JOIN FETCH tsi.info
            JOIN FETCH tsi.teamMember tm
            JOIN FETCH tm.member
            WHERE tsi.teamMember.teamBottari.id = :teamBottariId
            """)
    List<TeamSharedItem> findAllByTeamBottariId(final Long teamBottariId);

    List<TeamSharedItem> findAllByTeamMemberIn(final List<TeamMember> teamMembers);

    @Query("""
            SELECT tsi
            FROM TeamSharedItem tsi
            JOIN FETCH tsi.info
            WHERE tsi.teamMember.id = :teamMemberId
            """)
    List<TeamSharedItem> findAllByTeamMemberId(final Long teamMemberId);
}
