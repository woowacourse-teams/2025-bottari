package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmSendTaskRepository extends JpaRepository<FcmSendTask, Long> {
}
