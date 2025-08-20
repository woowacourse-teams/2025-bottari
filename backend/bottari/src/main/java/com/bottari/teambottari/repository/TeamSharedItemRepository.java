package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamSharedItemRepository extends JpaRepository<TeamSharedItem, Long> {

    @Query("""
            SELECT tsi
            FROM TeamSharedItem tsi
            JOIN FETCH tsi.teamMember tm
            WHERE tsi.id = :id
            """)
    Optional<TeamSharedItem> findByIdWithTeamMember(final Long id);

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

    void deleteAllByInfo(final TeamSharedItemInfo teamSharedItemInfo);

    @Modifying
    @Query("""
            UPDATE TeamSharedItem tsi
            SET tsi.deletedAt = CURRENT_TIMESTAMP
            WHERE tsi.teamMember.id = :teamMemberId
            """)
    void deleteByTeamMemberId(final Long teamMemberId);

    @Query("""
            SELECT tsi
            FROM TeamSharedItem tsi
            JOIN FETCH tsi.teamMember tm
            JOIN FETCH tm.member
            WHERE tsi.info.id = :infoId
            """)
    List<TeamSharedItem> findAllByInfoIdWithMember(final Long infoId);
}
