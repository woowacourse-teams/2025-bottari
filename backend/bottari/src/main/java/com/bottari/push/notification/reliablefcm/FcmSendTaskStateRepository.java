package com.bottari.push.notification.reliablefcm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmSendTaskStateRepository extends JpaRepository<FcmSendTaskState, Long> {
}
