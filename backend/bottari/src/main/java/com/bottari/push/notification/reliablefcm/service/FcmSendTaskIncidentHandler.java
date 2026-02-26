package com.bottari.push.notification.reliablefcm.service;

import com.bottari.alert.AlertService;
import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FcmSendTaskIncidentHandler {

    private final FcmSendTaskService fcmSendTaskService;
    private final FcmSendFailedTaskService fcmSendFailedTaskService;
    private final AlertService alertService;

    /*
    IN_PROGRESS 상태로 오래 머문 작업들을 찾아서 재시도하거나 실패로 처리하는 스케줄러
    전송 작업 수행 후, 작업 결과를 DB에 반영하지 못한 Task들을 복구하기 위함
     */
    @Scheduled(fixedRate = 10_000) // 10초
    @Transactional
    public void handleStuckTasks() {
        final List<FcmSendTask> tasks = fcmSendTaskService.getStuckTasks(Duration.ofMinutes(1), 100);
        for (final FcmSendTask task : tasks) {
            if (task.isRetryLimitExceeded()) {
                fcmSendTaskService.failTask(task.getId(), FailedCause.RETRY_LIMIT_EXCEEDED, task.getClaimId());
                continue;
            }
            fcmSendTaskService.retryTask(task.getId(), Duration.ZERO, task.getClaimId());
        }
    }

    @Scheduled(fixedRate = 60_000) // 1분
    public void handleFailedTasks() {
        final List<FcmSendFailedTask> failedTasks = fcmSendFailedTaskService.getPendingFailedTasks(100);
        alertService.send(buildAlertMessage(failedTasks));
        final List<Long> failedTaskIds = failedTasks.stream()
                .map(FcmSendFailedTask::getId)
                .toList();
        fcmSendFailedTaskService.alertedFailedTasks(failedTaskIds);
    }

    private String buildAlertMessage(final List<FcmSendFailedTask> failedTasks) {
        final StringBuilder sb = new StringBuilder();
        sb.append("The following FCM send tasks have failed:\n");
        for (final FcmSendFailedTask task : failedTasks) {
            sb.append("Task ID: ")
                    .append(task.getFcmSendTask().getId())
                    .append(", Cause: ")
                    .append(task.getFailedCause())
                    .append(", Failed At: ")
                    .append(task.getFailedAt())
                    .append("\n");
        }

        return sb.toString();
    }
}
