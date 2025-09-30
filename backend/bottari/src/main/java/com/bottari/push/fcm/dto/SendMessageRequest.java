package com.bottari.push.fcm.dto;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SendMessageRequest {

    private final Map<String, String> data;
    private final MessageType messageType;
}
