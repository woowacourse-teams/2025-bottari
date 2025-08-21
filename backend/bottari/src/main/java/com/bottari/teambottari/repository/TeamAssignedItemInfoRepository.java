package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamAssignedItemInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamAssignedItemInfoRepository extends JpaRepository<TeamAssignedItemInfo, Long> {

    @Query("""
            SELECT taii
            FROM TeamAssignedItemInfo taii
            WHERE taii.teamBottari.id = :teamBottariId
            """)
    List<TeamAssignedItemInfo> findAllByTeamBottariId(final Long teamBottariId);

    @Query("""
            SELECT COUNT(taii) > 0
            FROM TeamAssignedItemInfo taii
            WHERE taii.teamBottari.id = :teamBottariId
              AND taii.name.name = :name
            """)
    boolean existsByTeamBottariIdAndName(
            final Long teamBottariId,
            final String name
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE TeamAssignedItemInfo tai
            SET tai.deletedAt = CURRENT_TIMESTAMP 
            WHERE tai.id in :ids
            """)
    void deleteByInfoIds(final List<Long> ids);
}
