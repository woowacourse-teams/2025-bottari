package com.bottari.push.fcm.dto;

import java.util.Map;

public record SendMessageRequest(
        Map<String, String> data,
        MessageType messageType
) {
}
