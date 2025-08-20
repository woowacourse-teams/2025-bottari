package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamSharedItemInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamSharedItemInfoRepository extends JpaRepository<TeamSharedItemInfo, Long> {

    @Query("""
             SELECT tsii
             FROM TeamSharedItemInfo tsii
             WHERE tsii.teamBottari.id = :teamBottariId
            """)
    List<TeamSharedItemInfo> findAllByTeamBottariId(final Long teamBottariId);

    @Query("""
             SELECT COUNT(tsii) > 0
             FROM TeamSharedItemInfo tsii
             WHERE tsii.teamBottari.id = :teamBottariId
               AND tsii.name.name = :name
            """)
    boolean existsByTeamBottariIdAndName(
            final Long teamBottariId,
            final String name
    );

    @Modifying
    @Query("""
        UPDATE TeamSharedItemInfo tsi
        SET tsi.deletedAt = CURRENT_TIMESTAMP 
        WHERE tsi.teamBottari.id = :teamBottariId
        """)
    void deleteByTeamBottariId(final Long teamBottariId);
}
