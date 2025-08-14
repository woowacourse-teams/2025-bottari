package com.bottari.fcm.dto;

import java.util.Map;
import java.util.stream.Collectors;

public record SendMessageRequest(
        Map<String, Object> data,
        MessageType messageType
) {

    public Map<String, String> dataToJsonValue() {
        return data.keySet()
                .stream()
                .collect(Collectors.toMap(key -> key, key -> data.get(key).toString()));
    }
}
