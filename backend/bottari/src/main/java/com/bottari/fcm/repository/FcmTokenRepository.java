package com.bottari.fcm.repository;

import com.bottari.fcm.domain.FcmToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByMemberSsaid(String ssaid);
}
