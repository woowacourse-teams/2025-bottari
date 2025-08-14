package com.bottari.teambottari.repository;

import com.bottari.teambottari.domain.TeamBottari;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBottariRepository extends JpaRepository<TeamBottari, Long> {

    Optional<TeamBottari> findByInviteCode(final String inviteCode);
}
