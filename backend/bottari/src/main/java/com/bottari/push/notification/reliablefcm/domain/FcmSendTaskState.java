package com.bottari.push.notification.reliablefcm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FcmSendTaskState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fcm_send_task_id")
    private FcmSendTask fcmSendTask;

    @Enumerated(EnumType.STRING)
    private TaskState state;

    private LocalDateTime createdAt;

    public FcmSendTaskState(
            final FcmSendTask fcmSendTask,
            final TaskState state
    ) {
        this.fcmSendTask = fcmSendTask;
        this.state = state;
        this.createdAt = LocalDateTime.now();
    }
}
