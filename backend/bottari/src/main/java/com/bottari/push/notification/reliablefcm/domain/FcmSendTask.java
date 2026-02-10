package com.bottari.push.notification.reliablefcm.domain;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import com.bottari.push.message.PushMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FcmSendTask {

    public static final int MAX_ATTEMPT_COUNT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TaskState state;

    @Enumerated(EnumType.STRING)
    private FailedCause failedCause;

    private LocalDateTime scheduledAt;

    private LocalDateTime inProgressAt;

    private LocalDateTime finishedAt;

    private int attemptCount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false)
    private PushMessage message;

    private Long targetMemberId;

    public FcmSendTask(
            final LocalDateTime scheduledAt,
            final PushMessage message,
            final Long targetMemberId
    ) {
        this.state = TaskState.PENDING;
        this.scheduledAt = scheduledAt;
        this.message = message;
        this.targetMemberId = targetMemberId;
        this.attemptCount = 0;
    }

    public void markPending(final LocalDateTime scheduledAt) {
        validateIsNotFinished();
        final TaskState markingState = TaskState.PENDING;
        validateIsSameState(markingState, this.state);
        this.state = markingState;
        this.scheduledAt = scheduledAt;
    }

    public void markInProgress() {
        validateIsNotFinished();
        final TaskState markingState = TaskState.IN_PROGRESS;
        validateIsSameState(markingState, this.state);
        this.state = TaskState.IN_PROGRESS;
        this.inProgressAt = LocalDateTime.now();
        this.attemptCount += 1;
    }

    public void markCompleted() {
        validateIsNotFinished();
        final TaskState markingState = TaskState.COMPLETED;
        validateIsSameState(markingState, this.state);
        this.state = TaskState.COMPLETED;
        this.finishedAt = LocalDateTime.now();
    }

    public void markFailed(final FailedCause failedCause) {
        validateIsNotFinished();
        final TaskState markingState = TaskState.FAILED;
        validateIsSameState(markingState, this.state);
        this.state = TaskState.FAILED;
        this.finishedAt = LocalDateTime.now();
        this.failedCause = failedCause;
    }

    private void validateIsNotFinished() {
        if (isFinish()) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_ALREADY_FINISHED, "taskId: " + this.id);
        }
    }

    private boolean isFinish() {
        return this.state == TaskState.COMPLETED || this.state == TaskState.FAILED;
    }

    private void validateIsSameState(
            final TaskState markingState,
            final TaskState state
    ) {
        if (markingState == state) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_SAME_STATE, "taskId: " + this.id + ", state: " + state);
        }
    }
}
