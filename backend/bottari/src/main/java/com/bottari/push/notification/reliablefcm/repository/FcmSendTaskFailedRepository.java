package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FcmSendTaskFailedRepository extends JpaRepository<FcmSendFailedTask, Long> {

    @Query(value = """
            SELECT *
            FROM fcm_send_failed_task
            WHERE state = 'PENDING'
            LIMIT :limit
            """, nativeQuery = true)
    List<FcmSendFailedTask> findPendingFailedTasks(final int limit);
}
