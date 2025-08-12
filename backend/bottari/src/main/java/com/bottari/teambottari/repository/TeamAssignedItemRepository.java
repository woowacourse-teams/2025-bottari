package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItem;
import com.bottari.teambottari.domain.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamAssignedItemRepository extends JpaRepository<TeamAssignedItem, Long> {

    List<TeamAssignedItem> findAllByTeamMemberIn(List<TeamMember> teamMembers);
}
