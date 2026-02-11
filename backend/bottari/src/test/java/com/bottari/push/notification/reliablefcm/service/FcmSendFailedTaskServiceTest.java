package com.bottari.push.notification.reliablefcm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.push.notification.reliablefcm.domain.FailedCause;
import com.bottari.push.notification.reliablefcm.domain.FailedTaskState;
import com.bottari.push.notification.reliablefcm.domain.FcmSendFailedTask;
import jakarta.persistence.EntityManager;
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
        final FcmSendFailedTask task1 = new FcmSendFailedTask(null, FailedCause.RETRY_LIMIT_EXCEEDED);
        final FcmSendFailedTask task2 = new FcmSendFailedTask(null, FailedCause.RETRY_LIMIT_EXCEEDED);
        final FcmSendFailedTask task3 = new FcmSendFailedTask(null, FailedCause.RETRY_LIMIT_EXCEEDED);
        task3.markAlerted();
        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);
        entityManager.flush();

        // when
        final List<FcmSendFailedTask> tasks = fcmSendFailedTaskService.getPendingFailedTasks();

        // then
        assertAll(
                () -> assertThat(tasks.size()).isEqualTo(2),
                () -> assertThat(tasks).containsExactlyInAnyOrder(task1, task2)
        );
    }

    @DisplayName("FCM 전송 실패 작업들의 상태를 ALERTED로 변경한다.")
    @Test
    void alertedFailedTasks() {
        // given
        final FcmSendFailedTask task1 = new FcmSendFailedTask(null, FailedCause.RETRY_LIMIT_EXCEEDED);
        final FcmSendFailedTask task2 = new FcmSendFailedTask(null, FailedCause.RETRY_LIMIT_EXCEEDED);
        task1.markAlerted();
        task2.markAlerted();
        entityManager.persist(task1);
        entityManager.persist(task2);

        // when
        fcmSendFailedTaskService.alertedFailedTasks(List.of(task1.getId(), task2.getId()));
        entityManager.flush();

        // then
        final List<FcmSendFailedTask> actual = entityManager.createQuery(
                        "SELECT t FROM FcmSendFailedTask t WHERE t.id IN (:task1Id,:task2Id)",
                        FcmSendFailedTask.class)
                .setParameter("task1Id", task1.getId())
                .setParameter("task2Id", task2.getId())
                .getResultList();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).extracting(FcmSendFailedTask::getState)
                        .containsExactly(FailedTaskState.ALERTED, FailedTaskState.ALERTED)
        );
    }
}
