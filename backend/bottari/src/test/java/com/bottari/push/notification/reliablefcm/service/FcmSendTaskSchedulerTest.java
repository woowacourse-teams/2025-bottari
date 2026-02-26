package com.bottari.push.notification.reliablefcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.fcm.service.FcmChannel;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FcmSendTaskSchedulerTest {

    @Mock
    private FcmSendTaskService fcmSendTaskService;

    @Mock
    private FcmChannel fcmChannel;

    @InjectMocks
    private FcmSendTaskScheduler fcmSendTaskScheduler;

    private static Stream<Arguments> provideAttemptAndDuration() {
        return Stream.of(
                Arguments.of(1, Duration.ofMinutes(1)),
                Arguments.of(2, Duration.ofMinutes(2)),
                Arguments.of(3, Duration.ofMinutes(4))
        );
    }

    @DisplayName("FCM 전송이 성공하면 작업을 완료 처리한다")
    @Test
    void success() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        verify(fcmChannel, times(1)).unicast(any(), eq(1L));
        verify(fcmSendTaskService, times(1)).completeTask(task.getId());
        verify(fcmSendTaskService, never()).retryTask(any(), any());
        verify(fcmSendTaskService, never()).failTask(any(), any());
    }

    @DisplayName("FCM 토큰 문제로 실패하면 작업을 실패 처리한다")
    @Test
    void failed_invalid_token() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);
        doThrow(new BusinessException(ErrorCode.FCM_INVALID_TOKEN))
                .when(fcmChannel)
                .unicast(any(), eq(1L));

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        verify(fcmChannel, times(1)).unicast(any(), eq(1L));
        verify(fcmSendTaskService, times(1)).failTask(eq(task.getId()), any());
        verify(fcmSendTaskService, never()).completeTask(any());
        verify(fcmSendTaskService, never()).retryTask(any(), any());
    }

    @DisplayName("FCM 전송 실패 시 작업을 재시도 처리한다 - BusinessException")
    @Test
    void retry() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);
        doThrow(new BusinessException(ErrorCode.FCM_MESSAGE_SEND_FAIL))
                .when(fcmChannel)
                .unicast(any(), eq(1L));

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        verify(fcmChannel, times(1)).unicast(any(), eq(1L));
        verify(fcmSendTaskService, times(1)).retryTask(eq(task.getId()), any());
        verify(fcmSendTaskService, never()).failTask(any(), any());
        verify(fcmSendTaskService, never()).completeTask(any());
    }

    @DisplayName("FCM 전송 실패 시 작업을 재시도 처리한다 - RuntimeException")
    @Test
    void retry2() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);
        doThrow(new RuntimeException())
                .when(fcmChannel)
                .unicast(any(), eq(1L));

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        verify(fcmChannel, times(1)).unicast(any(), eq(1L));
        verify(fcmSendTaskService, times(1)).retryTask(eq(task.getId()), any());
        verify(fcmSendTaskService, never()).failTask(any(), any());
        verify(fcmSendTaskService, never()).completeTask(any());
    }

    @DisplayName("FCM 전송 실패 및 작업 시도 횟수 초과 시 실패 처리한다 - BusinessException")
    @Test
    void failed_retry_limit_exceeded() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);
        when(task.isRetryLimitExceeded()).thenReturn(true);
        doThrow(new BusinessException(ErrorCode.FCM_MESSAGE_SEND_FAIL))
                .when(fcmChannel)
                .unicast(any(), eq(1L));

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        verify(fcmChannel, times(1)).unicast(any(), eq(1L));
        verify(fcmSendTaskService, times(1)).failTask(eq(task.getId()), any());
        verify(fcmSendTaskService, never()).retryTask(any(), any());
        verify(fcmSendTaskService, never()).completeTask(any());
    }

    @DisplayName("FCM 전송 실패 및 작업 시도 횟수 초과 시 실패 처리한다 - RuntimeException")
    @Test
    void failed_retry_limit_exceeded2() {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);
        when(task.isRetryLimitExceeded()).thenReturn(true);
        doThrow(new RuntimeException())
                .when(fcmChannel)
                .unicast(any(), eq(1L));

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        verify(fcmChannel, times(1)).unicast(any(), eq(1L));
        verify(fcmSendTaskService, times(1)).failTask(eq(task.getId()), any());
        verify(fcmSendTaskService, never()).retryTask(any(), any());
        verify(fcmSendTaskService, never()).completeTask(any());
    }

    @DisplayName("재시도 횟수에 따라 retry delay 계산한다.")
    @ParameterizedTest
    @MethodSource("provideAttemptAndDuration")
    void calculateRetryDelay(
            final int attempt,
            final Duration expected
    ) {
        // given
        final FcmSendTask task = mock(FcmSendTask.class);

        when(fcmSendTaskService.claimPendingTasks(any(), eq(100))).thenReturn(List.of(task));
        when(task.getMessage()).thenReturn(mock(PushMessage.class));
        when(task.getTargetMemberId()).thenReturn(1L);
        when(task.getAttemptCount()).thenReturn(attempt);

        doThrow(new RuntimeException())
                .when(fcmChannel)
                .unicast(any(), eq(1L));

        // when
        fcmSendTaskScheduler.pollAndSendTasks();

        // then
        final ArgumentCaptor<Duration> captor = ArgumentCaptor.forClass(Duration.class);
        verify(fcmSendTaskService).retryTask(eq(task.getId()), captor.capture());
        assertThat(captor.getValue()).isEqualTo(expected);
    }
}
