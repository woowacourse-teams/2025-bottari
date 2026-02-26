package com.bottari.push.notification.reliablefcm.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class FcmSendFailedTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FailedCause failedCause;

    @Enumerated(EnumType.STRING)
    private FailedTaskState state;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(length = 16)
    private UUID claimId;

    private LocalDateTime failedAt;

    private LocalDateTime alertedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fcm_send_task_id")
    private FcmSendTask fcmSendTask;

    public FcmSendFailedTask(
            final FcmSendTask fcmSendTask,
            final FailedCause failedCause
    ) {
        this.fcmSendTask = fcmSendTask;
        this.failedCause = failedCause;
        this.state = FailedTaskState.PENDING;
        this.claimId = fcmSendTask.getClaimId();
        this.failedAt = LocalDateTime.now();
    }

    public void markAlerted() {
        this.state = FailedTaskState.ALERTED;
        this.alertedAt = LocalDateTime.now();
    }
}
