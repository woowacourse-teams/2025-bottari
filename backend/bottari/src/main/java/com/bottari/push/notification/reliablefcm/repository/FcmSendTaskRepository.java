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
                  AND attempt_count <= 3
                ORDER BY scheduled_at
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<FcmSendTask> findDuePendingTasksForUpdate(
            final LocalDateTime now,
            final int limit
    );

    @Query("""
            SELECT t
            FROM FcmSendTask t
            WHERE t.state = 'IN_PROGRESS'
              AND t.inProgressAt <= :thresholdTime
            """)
    List<FcmSendTask> findStuckInProgressTasks(final LocalDateTime thresholdTime);
}
