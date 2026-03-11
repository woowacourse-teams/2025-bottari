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
import jakarta.persistence.ManyToOne;
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
public class FcmSendTaskState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fcm_send_task_id")
    private FcmSendTask fcmSendTask;

    @Enumerated(EnumType.STRING)
    private TaskState state;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(length = 16)
    private UUID claimId;

    private LocalDateTime createdAt;

    public FcmSendTaskState(final FcmSendTask fcmSendTask) {
        this.fcmSendTask = fcmSendTask;
        this.state = fcmSendTask.getState();
        this.claimId = fcmSendTask.getClaimId();
        this.createdAt = LocalDateTime.now();
    }
}
