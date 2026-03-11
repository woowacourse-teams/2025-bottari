package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendTaskState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmSendTaskStateRepository extends JpaRepository<FcmSendTaskState, Long> {
}
