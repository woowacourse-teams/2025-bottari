package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamAssignedItemRepository extends JpaRepository<TeamAssignedItem, Long> {

    @Query("""
            SELECT tai
            FROM TeamAssignedItem tai
            JOIN FETCH tai.info
            WHERE tai.teamMember.id = :teamMemberId
            """)
    List<TeamAssignedItem> findAllByTeamMemberId(final Long teamMemberId);
}
