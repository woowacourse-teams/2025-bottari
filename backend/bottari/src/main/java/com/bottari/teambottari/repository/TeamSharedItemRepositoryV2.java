package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamSharedItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamSharedItemRepositoryV2 extends JpaRepository<TeamSharedItem, Long> {
    List<TeamSharedItem> findAllByTeamMemberId(final Long id);
}
