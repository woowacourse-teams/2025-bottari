package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Optional<TeamMember> findByTeamBottariIdAndMemberId(
            final Long teamBottariId,
            final Long memberId
    );
}
