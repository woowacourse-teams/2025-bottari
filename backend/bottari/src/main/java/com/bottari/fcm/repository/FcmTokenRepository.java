package com.bottari.fcm.repository;

import com.bottari.fcm.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
}
