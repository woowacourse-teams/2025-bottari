package com.bottari.push.notification.reliablefcm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmSendTaskRepository extends JpaRepository<FcmSendTask, Long> {
}
