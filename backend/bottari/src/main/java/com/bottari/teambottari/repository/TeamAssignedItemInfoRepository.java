package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamAssignedItemInfoRepository extends JpaRepository<TeamAssignedItemInfo, Long> {

    @Query("""
            SELECT taii
            FROM TeamAssignedItemInfo taii
            WHERE taii.teamBottari.id = :teamBottariId
            """)
    List<TeamAssignedItemInfo> findAllByTeamBottariId(final Long teamBottariId);
}
