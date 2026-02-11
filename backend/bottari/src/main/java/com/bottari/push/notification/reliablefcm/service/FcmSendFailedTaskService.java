package com.bottari.push.notification.reliablefcm.service;

import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import com.bottari.push.notification.reliablefcm.repository.FcmSendTaskFailedRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmSendFailedTaskService {

    private final FcmSendTaskFailedRepository fcmSendTaskFailedRepository;

    @Transactional(readOnly = true)
    public List<FcmSendFailedTask> getPendingFailedTasks() {
        return fcmSendTaskFailedRepository.findPendingFailedTasks();
    }

    @Transactional
    public void alertedFailedTasks(final List<Long> failedTaskIds) {
        final List<FcmSendFailedTask> failedTasks = fcmSendTaskFailedRepository.findAllById(failedTaskIds);
        failedTasks.forEach(FcmSendFailedTask::markAlerted);
    }
}
