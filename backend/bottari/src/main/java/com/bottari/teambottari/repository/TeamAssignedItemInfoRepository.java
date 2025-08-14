package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import com.bottari.teambottari.domain.TeamSharedItemInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamAssignedItemInfoRepository extends JpaRepository<TeamSharedItemInfo, Long> {

    @Query("""
            SELECT taii
            FROM TeamAssignedItemInfo taii
            WHERE taii.teamBottari.id = :teamBottariId
            """)
    List<TeamAssignedItemInfo> findAllByTeamBottariId(final Long teamBottariId);
}
