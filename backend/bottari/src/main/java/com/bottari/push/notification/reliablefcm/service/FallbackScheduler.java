package com.bottari.push.notification.reliablefcm.service;

import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FallbackScheduler {

    private final FcmSendTaskService fcmSendTaskService;

    @Scheduled(fixedRate = 10_000) // 10초
    @Transactional
    public void recoverStuckTasks() {
        final List<FcmSendTask> tasks = fcmSendTaskService.getStuckTasks(Duration.ofMinutes(1));
        tasks.forEach(task -> fcmSendTaskService.retryTask(task, Duration.ZERO));
    }
}
