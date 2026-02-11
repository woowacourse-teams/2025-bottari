package com.bottari.push.notification.reliablefcm.service;

import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTaskState;
import com.bottari.push.notification.reliablefcm.domain.TaskState;
import com.bottari.push.notification.reliablefcm.dto.ScheduleFcmSendTaskRequest;
import com.bottari.push.notification.reliablefcm.repository.FcmSendTaskFailedRepository;
import com.bottari.push.notification.reliablefcm.repository.FcmSendTaskRepository;
import com.bottari.push.notification.reliablefcm.repository.FcmSendTaskStateRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmSendTaskService {

    private final FcmSendTaskRepository fcmSendTaskRepository;
    private final FcmSendTaskStateRepository fcmSendTaskStateRepository;
    private final FcmSendTaskFailedRepository fcmSendTaskFailedRepository;

    @Transactional
    public List<FcmSendTask> claimPendingTasks(final int limit) {
        final LocalDateTime now = LocalDateTime.now();
        final List<FcmSendTask> tasks =
                fcmSendTaskRepository.findDuePendingTasksForUpdate(now, FcmSendTask.MAX_ATTEMPT_COUNT, limit);
        for (final FcmSendTask task : tasks) {
            task.markInProgress();
            final FcmSendTaskState state = new FcmSendTaskState(task, TaskState.IN_PROGRESS);
            fcmSendTaskStateRepository.save(state);
        }

        return tasks;
    }

    @Transactional(readOnly = true)
    public List<FcmSendTask> getStuckTasks(final Duration stuckDuration) {
        final LocalDateTime thresholdTime = LocalDateTime.now().minus(stuckDuration);

        return fcmSendTaskRepository.findStuckInProgressTasks(thresholdTime);
    }

    @Transactional
    public Long scheduleFcmSendTask(final ScheduleFcmSendTaskRequest request) {
        final FcmSendTask task = new FcmSendTask(
                request.scheduledAt(),
                request.message(),
                request.targetMemberId()
        );
        final FcmSendTaskState state = new FcmSendTaskState(task, TaskState.PENDING);
        fcmSendTaskRepository.save(task);
        fcmSendTaskStateRepository.save(state);

        return task.getId();
    }

    @Transactional
    public void scheduleFcmSendTasks(final List<ScheduleFcmSendTaskRequest> request) {
        /*
        배치 insert 사용 의도였으나, ID Identity Generation 전략 때문에 어려움이 있음.
         */
        final List<FcmSendTask> tasks = request.stream()
                .map(r -> new FcmSendTask(
                        r.scheduledAt(),
                        r.message(),
                        r.targetMemberId()
                ))
                .toList();
        final List<FcmSendTaskState> states = tasks.stream()
                .map(task -> new FcmSendTaskState(task, TaskState.PENDING))
                .toList();
        fcmSendTaskRepository.saveAll(tasks);
        fcmSendTaskStateRepository.saveAll(states);
    }

    @Transactional
    public void completeTask(final FcmSendTask task) {
        task.markCompleted();
        final FcmSendTaskState state = new FcmSendTaskState(task, TaskState.COMPLETED);
        fcmSendTaskStateRepository.save(state);
    }

    @Transactional
    public void retryTask(
            final FcmSendTask task,
            final Duration retryDelay
    ) {
        final LocalDateTime scheduledAt = LocalDateTime.now().plus(retryDelay);
        task.markPending(scheduledAt);
        final FcmSendTaskState state = new FcmSendTaskState(task, TaskState.PENDING);
        fcmSendTaskStateRepository.save(state);
    }

    @Transactional
    public void failTask(
            final FcmSendTask task,
            final FailedCause failedCause
    ) {
        task.markFailed();
        final FcmSendTaskState state = new FcmSendTaskState(task, TaskState.FAILED);
        fcmSendTaskStateRepository.save(state);
        final FcmSendFailedTask failedTask = new FcmSendFailedTask(task, failedCause);
        fcmSendTaskFailedRepository.save(failedTask);
    }
}
