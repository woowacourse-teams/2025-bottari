package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamPersonalItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamPersonalItemRepository extends JpaRepository<TeamPersonalItem, Long> {

    List<TeamPersonalItem> findAllByTeamMemberId(final Long teamMemberId);
}
