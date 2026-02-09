package com.bottari.push.notification.reliablefcm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bottari.push.message.PushMessage;
import com.bottari.push.notification.reliablefcm.domain.FcmSendTaskState;
import com.bottari.push.notification.reliablefcm.domain.TaskState;
import com.bottari.push.notification.reliablefcm.dto.ScheduleFcmSendTaskRequest;
import com.bottari.push.notification.reliablefcm.service.FcmSendTaskService;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
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
}
