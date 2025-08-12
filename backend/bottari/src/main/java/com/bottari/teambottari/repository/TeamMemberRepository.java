package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
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
}
