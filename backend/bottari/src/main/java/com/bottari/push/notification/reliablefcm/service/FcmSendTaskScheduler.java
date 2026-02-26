package com.bottari.push.notification.reliablefcm.service;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.push.notification.fcm.service.FcmChannel;
import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.github.f4b6a3.uuid.UuidCreator;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FcmSendTaskScheduler {

    private final FcmSendTaskService fcmSendTaskService;
    private final FcmChannel fcmChannel;

    @Scheduled(fixedRate = 3_000) // 3초
    public void pollAndSendTasks() {
        final UUID claimId = UuidCreator.getTimeOrderedEpochFast();
        final List<FcmSendTask> tasks = fcmSendTaskService.claimPendingTasks(claimId, 100);
        for (final FcmSendTask task : tasks) {
            try {
                fcmChannel.unicast(task.getMessage(), task.getTargetMemberId());
                // 1) 전송 성공: 작업 완료 처리
                fcmSendTaskService.completeTask(task.getId());
            } catch (final BusinessException e) {
                handleBusinessException(task, e);
            } catch (final Exception e) {
                handleRetryOrFail(task);
            }
        }
    }

    private void handleBusinessException(
            final FcmSendTask task,
            final BusinessException e
    ) {
        // 2) 토큰 문제: 영구 실패
        if (isPermanentError(e.getErrorCode())) {
            fcmSendTaskService.failTask(task.getId(), FailedCause.INVALID_TOKEN);
            return;
        }
        handleRetryOrFail(task);
    }

    private void handleRetryOrFail(final FcmSendTask task) {
        // 3-1) 전송 실패(일시적): 시도 횟수 초과 시 영구 실패
        if (task.isRetryLimitExceeded()) {
            fcmSendTaskService.failTask(task.getId(), FailedCause.RETRY_LIMIT_EXCEEDED);
            return;
        }
        // 3-2) 전송 실패(일시적): 시도 횟수 남을 시 재시도
        fcmSendTaskService.retryTask(task.getId(), calculateRetryDelay(task));
    }

    private boolean isPermanentError(final ErrorCode errorCode) {
        return ErrorCode.FCM_TOKEN_NOT_FOUND == errorCode
               || ErrorCode.FCM_INITIALIZED_FAIL == errorCode
               || ErrorCode.FCM_INVALID_TOKEN == errorCode
               || ErrorCode.FCM_MESSAGE_CONVERT_FAIL == errorCode;
    }

    private Duration calculateRetryDelay(final FcmSendTask task) {
        final int attempt = task.getAttemptCount();
        // 1, 2, 4 ... 분 단위 백오프
        return Duration.ofMinutes((long) Math.pow(2, attempt - 1));
    }
}
