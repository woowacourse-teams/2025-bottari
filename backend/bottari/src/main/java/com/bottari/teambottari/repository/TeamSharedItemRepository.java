package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamMember;
import com.bottari.teambottari.domain.TeamSharedItem;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamSharedItemRepository extends JpaRepository<TeamSharedItem, Long> {

    List<TeamSharedItem> findAllByTeamMemberIn(Collection<TeamMember> teamMembers);
}
