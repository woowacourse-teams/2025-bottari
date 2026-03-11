package com.bottari.push.notification.reliablefcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FailedTaskState;
import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTask;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
        FcmSendFailedTaskService.class
)
class FcmSendFailedTaskServiceTest {

    @Autowired
    private FcmSendFailedTaskService fcmSendFailedTaskService;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("PENDING 상태의 실패한 FCM 전송 실패 작업들을 조회한다.")
    @Test
    void getPendingFailedTasks() {
        // given
        final FcmSendTask task1 = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );
        task1.markInProgress(UuidCreator.getTimeOrderedEpochFast());
        final FcmSendTask task2 = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );
        task2.markInProgress(UuidCreator.getTimeOrderedEpochFast());
        final FcmSendTask task3 = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );
        task3.markInProgress(UuidCreator.getTimeOrderedEpochFast());
        final FcmSendFailedTask failedTask1 = new FcmSendFailedTask(task1, FailedCause.RETRY_LIMIT_EXCEEDED);
        final FcmSendFailedTask failedTask2 = new FcmSendFailedTask(task2, FailedCause.RETRY_LIMIT_EXCEEDED);
        final FcmSendFailedTask failedTask3 = new FcmSendFailedTask(task3, FailedCause.RETRY_LIMIT_EXCEEDED);
        failedTask3.markAlerted();
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);
        entityManager.persist(failedTask1);
        entityManager.persist(failedTask2);
        entityManager.persist(failedTask3);
        entityManager.flush();

        // when
        final List<FcmSendFailedTask> tasks = fcmSendFailedTaskService.getPendingFailedTasks(100);

        // then
        assertAll(
                () -> assertThat(tasks.size()).isEqualTo(2),
                () -> assertThat(tasks).containsExactlyInAnyOrder(failedTask1, failedTask2)
        );
    }

    @DisplayName("FCM 전송 실패 작업들의 상태를 ALERTED로 변경한다.")
    @Test
    void alertedFailedTasks() {
        // given
        final FcmSendTask task1 = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );
        task1.markInProgress(UuidCreator.getTimeOrderedEpochFast());
        final FcmSendTask task2 = new FcmSendTask(
                LocalDateTime.now().plusMinutes(5),
                new PushMessage("test", "test", "null"),
                1L
        );
        task2.markInProgress(UuidCreator.getTimeOrderedEpochFast());
        final FcmSendFailedTask failedTask1 = new FcmSendFailedTask(task1, FailedCause.RETRY_LIMIT_EXCEEDED);
        final FcmSendFailedTask failedTask2 = new FcmSendFailedTask(task2, FailedCause.RETRY_LIMIT_EXCEEDED);
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(failedTask1);
        entityManager.persist(failedTask2);

        // when
        fcmSendFailedTaskService.alertedFailedTasks(List.of(failedTask1.getId(), failedTask2.getId()));
        entityManager.flush();

        // then
        final List<FcmSendFailedTask> actual = entityManager.createQuery(
                        "SELECT t FROM FcmSendFailedTask t WHERE t.id IN (:task1Id,:task2Id)",
                        FcmSendFailedTask.class)
                .setParameter("task1Id", failedTask1.getId())
                .setParameter("task2Id", failedTask2.getId())
                .getResultList();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).extracting(FcmSendFailedTask::getState)
                        .containsExactly(FailedTaskState.ALERTED, FailedTaskState.ALERTED)
        );
    }
}
