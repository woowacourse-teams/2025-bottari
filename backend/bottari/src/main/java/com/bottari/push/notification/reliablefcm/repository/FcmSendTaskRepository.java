package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t
            from FcmSendTask t
            where t.id = :taskId
           """)
    Optional<FcmSendTask> findByIdForUpdate(final Long taskId);
}
