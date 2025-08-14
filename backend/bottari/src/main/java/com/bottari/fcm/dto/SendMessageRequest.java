package com.bottari.fcm.dto;

import com.google.firebase.messaging.Notification;

public record SendMessageRequest(
        String title,
        String message,
        MessageType messageType
) {

    public Notification createNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(message)
                .build();
    }
}
