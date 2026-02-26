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
import java.util.UUID;
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

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(length = 16)
    private UUID claimId;

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
        if (this.state != TaskState.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_INVALID_STATE_TRANSITION, "current: " + this.state);
        }
        this.state = TaskState.PENDING;
        this.claimId = null;
        this.scheduledAt = scheduledAt;
    }

    public void markInProgress(final UUID claimId) {
        if (this.state != TaskState.PENDING) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_INVALID_STATE_TRANSITION, "current: " + this.state);
        }
        if (isRetryLimitExceeded()) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_INVALID_STATE_TRANSITION, "attempt exceeded");
        }
        if (claimId == null) {
            throw new IllegalArgumentException("claimId cannot be null");
        }
        this.state = TaskState.IN_PROGRESS;
        this.claimId = claimId;
        this.inProgressAt = LocalDateTime.now();
        this.attemptCount += 1;
    }

    public void markCompleted() {
        if (this.state != TaskState.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_INVALID_STATE_TRANSITION, "current: " + this.state);
        }
        this.state = TaskState.COMPLETED;
        this.finishedAt = LocalDateTime.now();
    }

    public void markFailed() {
        if (this.state != TaskState.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.FCM_SEND_TASK_INVALID_STATE_TRANSITION, "current: " + this.state);
        }
        this.state = TaskState.FAILED;
        this.finishedAt = LocalDateTime.now();
    }

    public boolean isRetryLimitExceeded() {
        return this.attemptCount >= MAX_ATTEMPT_COUNT;
    }

    public boolean isClaimedBy(final UUID claimId) {
        return this.claimId.equals(claimId);
    }
}
