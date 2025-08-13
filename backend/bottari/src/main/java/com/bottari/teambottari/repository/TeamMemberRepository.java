package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamBottari;
import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.dto.TeamBottariMemberCountProjection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

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
            JOIN FETCH tm.member m
            JOIN FETCH tm.teamBottari t
            WHERE m.id = :memberId
           """)
    List<TeamMember> findAllByMemberId(final Long memberId);

    @Query("""
            SELECT tm.teamBottari.id AS teamBottariId, COUNT(tm) AS memberCount
            FROM TeamMember tm
            WHERE tm.teamBottari IN :teamBottaries
            GROUP BY tm.teamBottari
           """)
    List<TeamBottariMemberCountProjection> countMembersByTeamBottariIn(final List<TeamBottari> teamBottaries);
}
