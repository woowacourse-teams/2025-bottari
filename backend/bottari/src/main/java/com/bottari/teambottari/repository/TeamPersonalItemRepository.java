package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamPersonalItemRepository extends JpaRepository<TeamPersonalItem, Long> {

    List<TeamPersonalItem> findAllByTeamMemberIn(final List<TeamMember> teamMembers);

    List<TeamPersonalItem> findAllByTeamMemberId(final Long teamMemberId);

    @Query("""
            SELECT COUNT(tpi) > 0
            FROM TeamPersonalItem tpi
            WHERE tpi.teamMember.id = :teamMemberId
              AND tpi.name.name = :name
            """)
    boolean existsByTeamMemberIdAndName(
            final Long teamMemberId,
            final String name
    );

    @Query("""
            SELECT tpi
            FROM TeamPersonalItem tpi
            WHERE tpi.teamMember.teamBottari.id = :teamBottariId
              AND tpi.teamMember.member.id = :memberId
            """)
    List<TeamPersonalItem> findAllByTeamBottariIdAndMemberId(
            final Long teamBottariId,
            final Long memberId
    );

    @Modifying(flushAutomatically = true)
    @Query("""
            UPDATE TeamPersonalItem tpi
            SET tpi.deletedAt = CURRENT_TIMESTAMP
            WHERE tpi.teamMember.id = :teamMemberId
            """)
    void deleteByTeamMemberId(final Long teamMemberId);
}
