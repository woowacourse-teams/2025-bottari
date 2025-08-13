package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamAssignedItemRepositoryV2 extends JpaRepository<TeamAssignedItem, Long> {
    List<TeamAssignedItem> findAllByTeamMemberId(final Long id);
}
