package com.bottari.push.notification.reliablefcm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FcmSendTaskIncidentHandlerTest {

    @Mock
    private FcmSendTaskService fcmSendTaskService;

    @InjectMocks
    private FcmSendTaskIncidentHandler fcmSendTaskIncidentHandler;

    @DisplayName("오래된 IN_PROGRESS 작업들 중 시도 횟수가 남은 작업은 재시도 처리한다")
    @Test
    void handleStuckTasks_retry() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.getStuckTasks(any())).thenReturn(List.of(task));
        when(task.isRetryLimitExceeded()).thenReturn(false);

        // when
        fcmSendTaskIncidentHandler.handleStuckTasks();

        // then
        verify(fcmSendTaskService, times(1))
                .retryTask(eq(task), eq(Duration.ZERO));
        verify(fcmSendTaskService, never()).failTask(any(), any());
    }

    @DisplayName("오래된 IN_PROGRESS 작업들 중 시도 횟수를 초과한 작업은 실패 처리한다")
    @Test
    void handleStuckTasks_failExceededRetryLimit() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.getStuckTasks(any())).thenReturn(List.of(task));
        when(task.isRetryLimitExceeded()).thenReturn(true);

        // when
        fcmSendTaskIncidentHandler.handleStuckTasks();

        // then
        verify(fcmSendTaskService, times(1))
                .failTask(eq(task), eq(FailedCause.RETRY_LIMIT_EXCEEDED));
        verify(fcmSendTaskService, never()).retryTask(any(), any());
    }
}
