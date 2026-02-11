package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmSendTaskFailedRepository extends JpaRepository<FcmSendFailedTask, Long> {
}
