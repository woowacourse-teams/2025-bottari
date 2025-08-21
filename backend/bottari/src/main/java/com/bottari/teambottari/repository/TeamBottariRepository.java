package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamBottari;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeamBottariRepository extends JpaRepository<TeamBottari, Long> {

    Optional<TeamBottari> findByInviteCode(final String inviteCode);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE TeamBottari tb
            SET tb.deletedAt = CURRENT_TIMESTAMP
            WHERE tb.id = :id
            """)
    void deleteById(final Long id);
}
