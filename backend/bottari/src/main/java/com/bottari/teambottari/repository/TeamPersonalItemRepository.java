package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamPersonalItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamPersonalItemRepository extends JpaRepository<TeamPersonalItem, Long> {

    List<TeamPersonalItem> findAllByTeamMemberIn(final List<TeamMember> teamMembers);
}
