package com.bottari.push.notification.reliablefcm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTaskState;
import com.bottari.push.notification.reliablefcm.domain.TaskState;
import com.bottari.push.notification.reliablefcm.dto.ScheduleFcmSendTaskRequest;
import com.bottari.push.notification.reliablefcm.service.FcmSendTaskService;
import jakarta.persistence.EntityManager;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
        FcmSendTaskService.class
)
class FcmSendTaskServiceTest {

    @Autowired
    private FcmSendTaskService fcmSendTaskService;

    @Autowired
    private EntityManager entityManager;

    @Nested
    class ClaimPendingTasks {

        @DisplayName("PENDING 상태의 FCM 전송 작업들을 조회하고 진행 중으로 상태를 변경한다.")
        @Test
        void claimPendingTasks() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().minusMinutes(1);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final Long taskId = fcmSendTaskService.scheduleFcmSendTask(request);

            // when
            final List<FcmSendTask> pollTasks = fcmSendTaskService.claimPendingTasks(10);

            // then
            final List<FcmSendTaskState> states = entityManager.createQuery(
                            "SELECT s FROM FcmSendTaskState s WHERE s.fcmSendTask.id = :taskId",
                            FcmSendTaskState.class
                    )
                    .setParameter("taskId", taskId)
                    .getResultList();

            assertAll(
                    () -> assertThat(pollTasks).hasSize(1),
                    () -> assertThat(pollTasks.getFirst().getId()).isEqualTo(taskId),
                    () -> assertThat(pollTasks.getFirst().getState()).isEqualTo(TaskState.IN_PROGRESS),
                    () -> assertThat(states).isNotNull(),
                    () -> assertThat(states).hasSize(2)
            );
        }

        @DisplayName("시도 횟수가 3회를 초과한 작업은 조회하지 않는다.")
        @Test
        void claimPendingTasks_attempt_count() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().minusMinutes(1);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final Long taskId = fcmSendTaskService.scheduleFcmSendTask(request);
            entityManager.flush();
            entityManager.createQuery("UPDATE FcmSendTask t SET t.attemptCount = 4 WHERE t.id = :taskId")
                    .setParameter("taskId", taskId)
                    .executeUpdate();
            entityManager.clear();

            // when
            final List<FcmSendTask> pollTasks = fcmSendTaskService.claimPendingTasks(10);

            assertThat(pollTasks).isEmpty();
        }

        @DisplayName("PENDING 상태가 아닌 작업은 조회하지 않는다.")
        @Test
        void claimPendingTasks_not_pending() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().minusMinutes(1);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final Long taskId = fcmSendTaskService.scheduleFcmSendTask(request);
            entityManager.flush();
            entityManager.createQuery("UPDATE FcmSendTask t SET t.state = :state WHERE t.id = :taskId")
                    .setParameter("state", TaskState.IN_PROGRESS)
                    .setParameter("taskId", taskId)
                    .executeUpdate();
            entityManager.clear();

            // when
            final List<FcmSendTask> pollTasks = fcmSendTaskService.claimPendingTasks(10);

            assertThat(pollTasks).isEmpty();
        }
    }

    @Nested
    class ScheduleFcmSendTask {

        @DisplayName("FCM 전송 작업을 스케줄링한다.")
        @Test
        void scheduleFcmSendTask() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().plusMinutes(10);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );

            // when
            final Long fcmSendTaskId = fcmSendTaskService.scheduleFcmSendTask(request);

            // then
            final FcmSendTaskState state = entityManager.createQuery(
                            "SELECT s FROM FcmSendTaskState s WHERE s.fcmSendTask.id = :taskId",
                            FcmSendTaskState.class
                    )
                    .setParameter("taskId", fcmSendTaskId)
                    .getSingleResult();

            assertAll(
                    () -> assertThat(fcmSendTaskId).isNotNull(),
                    () -> assertThat(state).isNotNull(),
                    () -> assertThat(state.getState()).isEqualTo(TaskState.PENDING)
            );
        }

        @DisplayName("FCM 전송 작업들을 스케줄링한다.")
        @Test
        void scheduleFcmSendTasks() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().plusMinutes(10);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final List<ScheduleFcmSendTaskRequest> requests = List.of(request, request, request);

            // when
            fcmSendTaskService.scheduleFcmSendTasks(requests);

            // then
            final List<FcmSendTask> tasks = entityManager.createQuery(
                            "SELECT t FROM FcmSendTask t",
                            FcmSendTask.class
                    )
                    .getResultList();
            final List<FcmSendTaskState> states = entityManager.createQuery(
                            "SELECT s FROM FcmSendTaskState s",
                            FcmSendTaskState.class
                    )
                    .getResultList();

            assertAll(
                    () -> assertThat(tasks).hasSize(3),
                    () -> assertThat(states).hasSize(3)
            );
        }
    }

    @Nested
    class CompleteTask {

        @DisplayName("FCM 전송 작업을 완료 처리한다.")
        @Test
        void completeTask() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().minusMinutes(1);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final Long taskId = fcmSendTaskService.scheduleFcmSendTask(request);
            final List<FcmSendTask> pollTasks = fcmSendTaskService.claimPendingTasks(10);
            final FcmSendTask task = pollTasks.getFirst();

            // when
            fcmSendTaskService.completeTask(task);

            // then
            final List<FcmSendTaskState> states = entityManager.createQuery(
                            "SELECT s FROM FcmSendTaskState s WHERE s.fcmSendTask.id = :taskId",
                            FcmSendTaskState.class
                    )
                    .setParameter("taskId", taskId)
                    .getResultList();

            states.sort(Comparator.comparing(FcmSendTaskState::getCreatedAt));
            final List<TaskState> orderedStates = states.stream()
                    .map(FcmSendTaskState::getState)
                    .toList();

            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.COMPLETED),
                    () -> assertThat(orderedStates)
                            .containsExactly(
                                    TaskState.PENDING,
                                    TaskState.IN_PROGRESS,
                                    TaskState.COMPLETED
                            )
            );
        }
    }

    @Nested
    class RetryTask {

        @DisplayName("FCM 전송 작업을 재시도 처리한다.")
        @Test
        void retryTask() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().minusMinutes(1);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final Long taskId = fcmSendTaskService.scheduleFcmSendTask(request);
            final List<FcmSendTask> pollTasks = fcmSendTaskService.claimPendingTasks(10);
            final FcmSendTask task = pollTasks.getFirst();
            final Duration retryDelay = Duration.ofMinutes(2);

            // when
            fcmSendTaskService.retryTask(task, retryDelay);

            // then
            final List<FcmSendTaskState> states = entityManager.createQuery(
                            "SELECT s FROM FcmSendTaskState s WHERE s.fcmSendTask.id = :taskId",
                            FcmSendTaskState.class
                    )
                    .setParameter("taskId", taskId)
                    .getResultList();

            states.sort(Comparator.comparing(FcmSendTaskState::getCreatedAt));
            final List<TaskState> orderedStates = states.stream()
                    .map(FcmSendTaskState::getState)
                    .toList();

            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.PENDING),
                    () -> assertThat(task.getAttemptCount()).isEqualTo(1),
                    () -> assertThat(task.getScheduledAt()).isAfter(scheduledAt),
                    () -> assertThat(orderedStates)
                            .containsExactly(
                                    TaskState.PENDING,
                                    TaskState.IN_PROGRESS,
                                    TaskState.PENDING
                            )
            );
        }
    }

    @Nested
    class FailTask {

        @DisplayName("FCM 전송 작업을 실패 처리한다.")
        @Test
        void failTask() {
            // given
            final LocalDateTime scheduledAt = LocalDateTime.now().minusMinutes(1);
            final PushMessage message = new PushMessage(
                    "test",
                    "test",
                    "test"
            );
            final Long targetMemberId = 1L;
            final ScheduleFcmSendTaskRequest request = new ScheduleFcmSendTaskRequest(
                    scheduledAt,
                    message,
                    targetMemberId
            );
            final Long taskId = fcmSendTaskService.scheduleFcmSendTask(request);
            final List<FcmSendTask> pollTasks = fcmSendTaskService.claimPendingTasks(10);
            final FcmSendTask task = pollTasks.getFirst();

            // when
            fcmSendTaskService.failTask(task);

            // then
            final List<FcmSendTaskState> states = entityManager.createQuery(
                            "SELECT s FROM FcmSendTaskState s WHERE s.fcmSendTask.id = :taskId",
                            FcmSendTaskState.class
                    )
                    .setParameter("taskId", taskId)
                    .getResultList();

            states.sort(Comparator.comparing(FcmSendTaskState::getCreatedAt));
            final List<TaskState> orderedStates = states.stream()
                    .map(FcmSendTaskState::getState)
                    .toList();

            assertAll(
                    () -> assertThat(task.getState()).isEqualTo(TaskState.FAILED),
                    () -> assertThat(orderedStates)
                            .containsExactly(
                                    TaskState.PENDING,
                                    TaskState.IN_PROGRESS,
                                    TaskState.FAILED
                            )
            );
        }
    }
}
