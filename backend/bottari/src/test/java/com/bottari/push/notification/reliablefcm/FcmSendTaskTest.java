package com.bottari.push.notification.reliablefcm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.error.BusinessException;
import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.bottari.push.notification.reliablefcm.domain.TaskState;
import com.github.f4b6a3.uuid.UuidCreator;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class FcmSendTaskTest {

    private static void setState(
            final FcmSendTask task,
            final TaskState state
    ) {
        try {
            final Field field = FcmSendTask.class.getDeclaredField("state");
            field.setAccessible(true);
            field.set(task, state);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void setAttemptCount(
            final FcmSendTask task,
            final int attemptCount
    ) {
        try {
            final Field field = FcmSendTask.class.getDeclaredField("attemptCount");
            field.setAccessible(true);
            field.set(task, attemptCount);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

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
                () -> assertThat(task.getTargetMemberId()).isEqualTo(targetMemberId),
                () -> assertThat(task.getClaimId()).isNull()
        );
    }

    @DisplayName("FcmSendTask의 재시도 한도 초과 여부를 확인한다.")
    @Test
    void isRetryLimitExceeded() {
        // given
        final FcmSendTask task = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );

        // when & then
        assertAll(
                () -> assertThat(task.isRetryLimitExceeded()).isFalse(),
                () -> {
                    setAttemptCount(task, 3);
                    assertThat(task.isRetryLimitExceeded()).isTrue();
                }
        );
    }

    @DisplayName("FcmSendTask의 점유자를 비교한다.")
    @Test
    void isClaimedBy() {
        // given
        final FcmSendTask task = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );
        final UUID uuid = UuidCreator.getTimeOrderedEpochFast();
        final UUID anotherUuid = UuidCreator.getTimeOrderedEpochFast();
        task.markInProgress(uuid);

        // when & then
        assertAll(
                () -> assertThat(task.isClaimedBy(uuid)).isTrue(),
                () -> assertThat(task.isClaimedBy(anotherUuid)).isFalse()
        );
    }

    @Nested
    class MarkPendingTest {

        @DisplayName("FcmSendTask 상태를 IN_PROGRESS에서 PENDING으로 변경한다.")
        @Test
        void markPending() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            setState(task, TaskState.IN_PROGRESS);
            final LocalDateTime scheduledAt = LocalDateTime.now().plusMinutes(5);

            // when
            task.markPending(scheduledAt);

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.PENDING),
                    () -> assertThat(task.getScheduledAt()).isEqualTo(scheduledAt),
                    () -> assertThat(task.getClaimId()).isNull()
            );
        }

        @DisplayName("FcmSendTask IN_PROGRESS가 아닌 상태에서 PENDING으로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(
                value = TaskState.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"IN_PROGRESS"}
        )
        void markPending_invalid_transition(final TaskState currentState) {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            setState(task, currentState);
            final LocalDateTime scheduledAt = LocalDateTime.now().plusMinutes(5);

            // when & then
            assertThatThrownBy(() -> task.markPending(scheduledAt))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업의 상태 전이가 올바르지 않습니다.");
        }
    }

    @Nested
    class MarkInProgressTest {

        @DisplayName("FcmSendTask 상태를 PENDING에서 IN_PROGRESS로 변경한다.")
        @Test
        void markInProgress() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            final UUID claimId = UuidCreator.getTimeOrderedEpochFast();

            // when
            task.markInProgress(claimId);

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.IN_PROGRESS),
                    () -> assertThat(task.getInProgressAt()).isNotNull(),
                    () -> assertThat(task.getAttemptCount()).isGreaterThanOrEqualTo(1),
                    () -> assertThat(task.getClaimId()).isEqualTo(claimId)
            );
        }

        @DisplayName("FcmSendTask PENDING이 아닌 상태에서 IN_PROGRESS로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(
                value = TaskState.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"PENDING"}
        )
        void markInProgress_invalid_transition(final TaskState currentState) {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            final UUID claimId = UuidCreator.getTimeOrderedEpochFast();
            setState(task, currentState);

            // when & then
            assertThatThrownBy(() -> task.markInProgress(claimId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업의 상태 전이가 올바르지 않습니다.");
        }
    }

    @Nested
    class MarkCompletedTest {

        @DisplayName("FcmSendTask 상태를 IN_PROGRESS에서 COMPLETED로 변경한다.")
        @Test
        void markCompleted() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            setState(task, TaskState.IN_PROGRESS);

            // when
            task.markCompleted();

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.COMPLETED),
                    () -> assertThat(task.getFinishedAt()).isNotNull()
            );
        }

        @DisplayName("FcmSendTask IN_PROGRESS가 아닌 상태에서 COMPLETED로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(
                value = TaskState.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"IN_PROGRESS"}
        )
        void markCompleted_invalid_transition(final TaskState currentState) {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            setState(task, currentState);

            // when & then
            assertThatThrownBy(task::markCompleted)
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업의 상태 전이가 올바르지 않습니다.");
        }
    }

    @Nested
    class MarkFailedTest {

        @DisplayName("FcmSendTask 상태를 IN_PROGRESS에서 FAILED로 변경한다.")
        @Test
        void markFailed() {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            setState(task, TaskState.IN_PROGRESS);

            // when
            task.markFailed();

            // then
            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.FAILED),
                    () -> assertThat(task.getFinishedAt()).isNotNull()
            );
        }

        @DisplayName("FcmSendTask IN_PROGRESS가 아닌 상태에서 FAILED로 변경하면 예외가 발생한다.")
        @ParameterizedTest
        @EnumSource(
                value = TaskState.class,
                mode = EnumSource.Mode.EXCLUDE,
                names = {"IN_PROGRESS"}
        )
        void markFailed_invalid_transition(final TaskState currentState) {
            // given
            final FcmSendTask task = new FcmSendTask(
                    LocalDateTime.now().plusMinutes(5),
                    new PushMessage("test", "test", "null"),
                    1L
            );
            setState(task, currentState);

            // when & then
            assertThatThrownBy(task::markFailed)
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("FCM 전송 작업의 상태 전이가 올바르지 않습니다.");
        }
    }
}
