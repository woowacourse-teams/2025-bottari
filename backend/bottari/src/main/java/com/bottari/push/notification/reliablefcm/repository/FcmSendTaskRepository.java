package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FcmSendTaskRepository extends JpaRepository<FcmSendTask, Long> {

    @Query(value = """
                SELECT *
                FROM fcm_send_task
                WHERE state = 'PENDING'
                  AND scheduled_at <= :now
                  AND attempt_count < :maxAttemptCount
                ORDER BY scheduled_at
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<FcmSendTask> findDuePendingTasksForUpdate(
            final LocalDateTime now,
            final int maxAttemptCount,
            final int limit
    );

    @Query(value = """
            SELECT *
            FROM fcm_send_task
            WHERE state = 'IN_PROGRESS'
              AND in_progress_at <= :thresholdTime
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<FcmSendTask> findStuckInProgressTasksForUpdate(
            final LocalDateTime thresholdTime,
            final int limit
    );
}
