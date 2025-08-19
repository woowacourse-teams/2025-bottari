package com.bottari.fcm.dto;

import java.util.Map;

public record SendMessageRequest(
        Map<String, String> data,
        MessageType messageType
) {
}
