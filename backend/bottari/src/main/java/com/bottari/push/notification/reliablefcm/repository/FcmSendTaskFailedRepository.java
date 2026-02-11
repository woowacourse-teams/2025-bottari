package com.bottari.push.notification.reliablefcm.repository;

import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FcmSendTaskFailedRepository extends JpaRepository<FcmSendFailedTask, Long> {

    @Query("""
            SELECT t
            FROM FcmSendFailedTask t
            WHERE t.state = 'PENDING'
            """)
    List<FcmSendFailedTask> findPendingFailedTasks();
}
