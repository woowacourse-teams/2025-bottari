package com.bottari.push.notification.reliablefcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmSendTaskService {

    private final FcmSendTaskRepository fcmSendTaskRepository;
    private final FcmSendTaskStateRepository fcmSendTaskStateRepository;

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
}
