package com.bottari.push.notification.reliablefcm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.bottari.push.notification.reliablefcm.domain.TaskState;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FcmSendTaskTest {

    @DisplayName("FcmSendTask를 생성한다.")
    @Test
    void createFcmSendTask() {
        // given
        final LocalDateTime scheduledAt = LocalDateTime.now().plusMinutes(5);
        final Long targetMemberId = 1L;
        final PushMessage message = new PushMessage(
                "test",
                "test",
                "null"
        );

        // when
        final FcmSendTask task = new FcmSendTask(
                scheduledAt,
                message,
                targetMemberId
        );

        // then
        assertAll(
                () -> assertThat(task.getState()).isEqualTo(TaskState.PENDING),
                () -> assertThat(task.getScheduledAt()).isEqualTo(scheduledAt),
                () -> assertThat(task.getInProgressAt()).isNull(),
                () -> assertThat(task.getFinishedAt()).isNull(),
                () -> assertThat(task.getAttemptCount()).isZero(),
                () -> assertThat(task.getMessage()).isEqualTo(message),
                () -> assertThat(task.getTargetMemberId()).isEqualTo(targetMemberId)
        );
    }

    @Nested
    class MarkStateTest {

        @DisplayName("FcmSendTask 상태를 PENDING으로 변경한다.")
        @Test
        void markPending() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            task.markInProgress();
            final LocalDateTime scheduledAt = LocalDateTime.now().plusMinutes(5);

            // when
            task.markPending(scheduledAt);

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.PENDING),
                    () -> assertThat(task.getScheduledAt()).isEqualTo(scheduledAt)
            );
        }

        @DisplayName("FcmSendTask 상태를 IN_PROGRESS로 변경한다.")
        @Test
        void markInProgress() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );

            // when
            task.markInProgress();

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.IN_PROGRESS),
                    () -> assertThat(task.getInProgressAt()).isNotNull(),
                    () -> assertThat(task.getAttemptCount()).isGreaterThanOrEqualTo(1)
            );
        }

        @DisplayName("FcmSendTask 상태를 COMPLETED로 변경한다.")
        @Test
        void markCompleted() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );

            // when
            task.markCompleted();

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.COMPLETED),
                    () -> assertThat(task.getFinishedAt()).isNotNull()
            );
        }

        @DisplayName("FcmSendTask 상태를 FAILED로 변경한다.")
        @Test
        void markFailed() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );

            // when
            task.markFailed(FailedCause.RETRY_LIMIT_EXCEEDED);

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.FAILED),
                    () -> assertThat(task.getFinishedAt()).isNotNull()
            );
        }
    }

    @Nested
    class InvalidCaseTest {

        @DisplayName("이미 완료된 FcmSendTask의 상태를 변경하려고 하면 예외가 발생한다. - IN_PROGRESS")
        @Test
        void changeStateAfterFinished1() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            task.markCompleted();

            // when & then
            assertThatThrownBy(task::markInProgress)
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업이 이미 완료되었습니다.");
        }

        @DisplayName("이미 완료된 FcmSendTask의 상태를 변경하려고 하면 예외가 발생한다. - FAILED")
        @Test
        void changeStateAfterFinished2() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            task.markFailed(FailedCause.INVALID_TOKEN);

            // when & then
            assertThatThrownBy(() -> task.markPending(LocalDateTime.now().plusMinutes(5)))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업이 이미 완료되었습니다.");
        }

        @DisplayName("현재 상태와 동일한 상태로 변경하려고 하면 예외가 발생한다. - PENDING")
        @Test
        void changeToSameState1() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );

            // when & then
            assertThatThrownBy(() -> task.markPending(LocalDateTime.now().plusMinutes(5)))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업이 이미 동일한 상태로 표시되어 있습니다.");
        }

        @DisplayName("현재 상태와 동일한 상태로 변경하려고 하면 예외가 발생한다. - IN_PROGRESS")
        @Test
        void changeToSameState2() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            task.markInProgress();

            // when & then
            assertThatThrownBy(task::markInProgress)
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업이 이미 동일한 상태로 표시되어 있습니다.");
        }
    }
}
