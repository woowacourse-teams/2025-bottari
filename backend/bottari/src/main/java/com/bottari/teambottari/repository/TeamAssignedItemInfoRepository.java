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
        UPDATE TeamAssignedItemInfo taii
        SET taii.deletedAt = CURRENT_TIMESTAMP
        WHERE taii IN (
                SELECT taii_sub
                FROM TeamAssignedItemInfo taii_sub
                LEFT JOIN TeamAssignedItem tai ON tai.info = taii_sub
                WHERE tai.id IS NULL
                  AND taii_sub.teamBottari.id = :teamBottariId
        )
        """)
    void deleteOrphanAssignedItemInfosByTeamBottariId(final Long teamBottariId);
}
