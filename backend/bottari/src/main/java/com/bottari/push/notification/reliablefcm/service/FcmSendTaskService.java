package com.bottari.push.notification.reliablefcm.service;

import com.bottari.push.notification.reliablefcm.dto.ScheduleFcmSendTaskRequest;
import com.bottari.push.notification.reliablefcm.domain.TaskState;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTaskState;
import com.bottari.push.notification.reliablefcm.repository.FcmSendTaskRepository;
import com.bottari.push.notification.reliablefcm.repository.FcmSendTaskStateRepository;
import java.util.List;
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
}
