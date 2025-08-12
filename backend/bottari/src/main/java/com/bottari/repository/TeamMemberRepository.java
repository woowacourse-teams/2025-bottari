package com.bottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    List<TeamMember> findAllByTeamBottariId(final Long teamBottariId);
}
