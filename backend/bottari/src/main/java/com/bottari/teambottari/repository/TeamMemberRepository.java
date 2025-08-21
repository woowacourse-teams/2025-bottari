package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.repository.dto.TeamBottariMemberCountProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<TeamMember> findByTeamBottariIdAndMemberId(
            final Long teamBottariId,
            final Long memberId
    );

    @Query("""
             SELECT tm
             FROM TeamMember tm
             JOIN FETCH tm.member m
             WHERE tm.teamBottari.id = :teamBottariId
            """)
    List<TeamMember> findAllByTeamBottariId(final Long teamBottariId);

    @Query("""
             SELECT tm
             FROM TeamMember tm
             JOIN FETCH tm.teamBottari t
             WHERE tm.member.id = :memberId
            """)
    List<TeamMember> findAllByMemberId(final Long memberId);

    @Query("""
             SELECT tm.teamBottari.id AS teamBottariId, COUNT(tm) AS memberCount
             FROM TeamMember tm
             WHERE tm.teamBottari IN :teamBottaries
             GROUP BY tm.teamBottari
            """)
    List<TeamBottariMemberCountProjection> countMembersByTeamBottariIn(final List<TeamBottari> teamBottaries);

    boolean existsByTeamBottariIdAndMemberId(
            final Long teamBottariId,
            final Long memberId
    );

    @Query("""
            SELECT tm
            FROM TeamMember tm
            WHERE tm.teamBottari.id = :teamBottariId
              AND tm.member.id IN :memberIds
            """)
    List<TeamMember> findAllByTeamBottariIdAndMemberIds(
            final Long teamBottariId,
            final List<Long> memberIds
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE TeamMember tm
            SET tm.deletedAt = CURRENT_TIMESTAMP
            WHERE tm.id = :id
            AND tm.deletedAt IS NULL
            """)
    void deleteById(final Long id);
}
